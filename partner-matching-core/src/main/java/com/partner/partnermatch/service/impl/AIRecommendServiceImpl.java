package com.partner.partnermatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.partner.partnermatch.common.Result;
import com.partner.partnermatch.dto.AIRecommendResponse;
import com.partner.partnermatch.dto.AIUserDto;
import com.partner.partnermatch.entity.Tag;
import com.partner.partnermatch.entity.User;
import com.partner.partnermatch.mapper.UserMapper;
import com.partner.partnermatch.mapper.UserTagMapper;
import com.partner.partnermatch.rag.LuceneStorageService;
import com.partner.partnermatch.rag.RAGTransferService;
import com.partner.partnermatch.rag.pojo.LuceneSearchResult;
import com.partner.partnermatch.service.AIRecommendService;
import com.partner.partnermatch.service.AIChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import com.fasterxml.jackson.core.type.TypeReference;

@Service
public class AIRecommendServiceImpl implements AIRecommendService {

    @Autowired
    private UserTagMapper userTagMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AIChatService aiChatService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private LuceneStorageService luceneStorageService;

    @Autowired
    private RAGTransferService ragTransferService;

    private static final Logger log = LoggerFactory.getLogger(AIRecommendServiceImpl.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final int WINDOW_SIZE = 10;

    private static final DefaultRedisScript<Long> CACHE_WRITE_SCRIPT =
            new DefaultRedisScript<>(
                    "redis.call('HSET', KEYS[1], 'pool', ARGV[1], 'cursor', ARGV[2]);" +
                    "redis.call('EXPIRE', KEYS[1], ARGV[3]);" +
                    "return 1;",
                    Long.class);

    @Override
    public Result<List<AIUserDto>> recommend(Long id) {
        if (id == null) {
            return Result.error(400, "用户ID不能为null");
        }
        String hashKey = "recommend:" + id;

        List<AIUserDto> fullList = null;
        int cursor = 0;

        // 1. 读 Hash：pool + cursor
        List<Object> cached = stringRedisTemplate.opsForHash()
                .multiGet(hashKey, List.of("pool", "cursor"));
        String poolJson = (cached != null && cached.size() > 1 && cached.get(0) != null)
                ? (String) cached.get(0) : null;

        if (poolJson != null) {
            try {
                fullList = objectMapper.readValue(poolJson, new TypeReference<List<AIUserDto>>() {});
                String cursorStr = cached.get(1) != null ? (String) cached.get(1) : "0";
                cursor = Integer.parseInt(cursorStr);
                if (cursor >= fullList.size()) {
                    cursor = 0;
                }
            } catch (Exception e) {
                log.warn("缓存反序列化失败: {}", e.getMessage());
                stringRedisTemplate.delete(hashKey);
                poolJson = null;
            }
        }

        if (poolJson == null) {
            fullList = refreshCandidateCache(id);
            if (fullList.isEmpty()) {
                return Result.error(404, "暂无匹配的伙伴");
            }
        }

        // 2. 游标窗口取最多 WINDOW_SIZE 人
        int toIndex = Math.min(cursor + WINDOW_SIZE, fullList.size());
        int actualSize = toIndex - cursor;
        List<AIUserDto> window = new ArrayList<>(fullList.subList(cursor, toIndex));
        Collections.shuffle(window);

        // 3. 游标按实际取出数量原子递增，触底删缓存（失败不影响推荐）
        try {
            Long nextCursor = stringRedisTemplate.opsForHash().increment(hashKey, "cursor", actualSize);
            if (nextCursor != null && nextCursor >= fullList.size()) {
                stringRedisTemplate.delete(hashKey);
            }
        } catch (Exception e) {
            log.warn("游标更新失败，跳过: {}", e.getMessage());
        }

        // 4. 当前用户信息
        User currentUser = userMapper.selectById(id);
        if (currentUser == null) {
            return Result.error(404, "用户不存在");
        }
        List<Tag> myTags = userTagMapper.findTagsByUserIds(Collections.singletonList(id)).stream()
                .map(row -> new Tag((Integer) row.get("id"), (String) row.get("tag")))
                .collect(Collectors.toList());

        // 5. AI 推荐
        String prompt = buildPrompt(currentUser, myTags, window);
        try {
            String aiResponse = aiChatService.chat(prompt);
            String cleaned = cleanAIResponse(aiResponse);
            AIRecommendResponse[] results = objectMapper.readValue(cleaned, AIRecommendResponse[].class);
            Map<Long, String> reasonMap = Arrays.stream(results)
                    .collect(Collectors.toMap(AIRecommendResponse::getId, AIRecommendResponse::getReason));
            List<AIUserDto> aiPickedList = new ArrayList<>();
            for (AIUserDto dto : window) {
                String reason = reasonMap.get(dto.getId());
                if (reason != null) {
                    dto.setBackground(reason);
                    aiPickedList.add(dto);
                }
            }
            if (!aiPickedList.isEmpty()) {
                return Result.success(aiPickedList); // AI 挑选成功，background 有值
            }
        } catch (Exception e) {
            log.warn("AI 推荐调用失败，返回窗口列表: {}", e.getMessage());
        }

        return Result.success(window); // AI 降级，background 为 null
    }

    @Override
    public List<AIUserDto> refreshCandidateCache(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为null");
        }
        String hashKey = "recommend:" + userId;

        // 优先尝试向量搜索，失败或结果不足时降级到 SQL 自连接
        List<Long> top50Ids = null;
        try {
            List<Map<String, Object>> myTagRows = userTagMapper.findTagsByUserIds(Collections.singletonList(userId));
            if (!myTagRows.isEmpty()) {
                String tagText = myTagRows.stream()
                        .map(row -> (String) row.get("tag"))
                        .collect(Collectors.joining(" "));
                float[] embedding = ragTransferService.encode(tagText);
                LuceneSearchResult result = luceneStorageService.searchByEmbedding(embedding, 51);
                if (result != null && result.getUserIds().length > 0) {
                    top50Ids = Arrays.stream(result.getUserIds())
                            .filter(uid -> uid != userId) // 排除自己
                            .limit(50)
                            .boxed()
                            .collect(Collectors.toList());
                }
            }
        } catch (Exception e) {
            log.warn("向量搜索失败，降级到 SQL 自连接 userId={}: {}", userId, e.getMessage());
        }

        // 降级：向量搜索未生效或结果不足 50 人，fallback 到 SQL
        if (top50Ids == null || top50Ids.size() < 50) {
            log.info("向量搜索降级到 SQL userId={} vectorResult={}", userId,
                    top50Ids == null ? "null" : top50Ids.size());
            top50Ids = userTagMapper.findMatchedUserIds(userId, 50);
        }

        if (top50Ids.isEmpty()) {
            stringRedisTemplate.delete(hashKey);
            return Collections.emptyList();
        }

        Collections.shuffle(top50Ids);

        Map<Long, User> userMap = userMapper.selectList(
                new QueryWrapper<User>().in("id", top50Ids)
        ).stream().collect(Collectors.toMap(User::getId, u -> u));

        Map<Long, List<Tag>> tagsMap = userTagMapper.findTagsByUserIds(top50Ids).stream()
                .collect(Collectors.groupingBy(
                        row -> (Long) row.get("user_id"),
                        Collectors.mapping(
                                row -> new Tag((Integer) row.get("id"), (String) row.get("tag")),
                                Collectors.toList()
                        )
                ));

        List<AIUserDto> recommendList = top50Ids.stream()
                .filter(userMap::containsKey)
                .map(uid -> new AIUserDto(
                        uid,
                        userMap.get(uid).getUsername(),
                        userMap.get(uid).getGender(),
                        null,
                        tagsMap.getOrDefault(uid, Collections.emptyList())
                ))
                .collect(Collectors.toList());

        try {
            String json = objectMapper.writeValueAsString(recommendList);
            stringRedisTemplate.execute(CACHE_WRITE_SCRIPT,
                    List.of(hashKey), json, "0", String.valueOf(3 * 3600));
        } catch (Exception e) {
            log.warn("缓存写入失败: {}", e.getMessage());
        }
        return recommendList;
    }

    private String cleanAIResponse(String raw) {
        String cleaned = raw.trim();
        if (cleaned.startsWith("```")) {
            int start = cleaned.indexOf("\n");
            cleaned = start > 0 ? cleaned.substring(start + 1) : cleaned.substring(3);
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }
        return cleaned.trim();
    }

    private String buildPrompt(User currentUser, List<Tag> myTags, List<AIUserDto> candidates) {
        try {
            Map<String, Object> currentUserJson = new LinkedHashMap<>();
            currentUserJson.put("昵称", currentUser.getUsername());
            currentUserJson.put("性别", "b".equals(currentUser.getGender()) ? "男" : "女");
            currentUserJson.put("标签", myTags.stream().map(Tag::getTag).collect(Collectors.toList()));

            List<Map<String, Object>> candidateList = new ArrayList<>();
            for (AIUserDto c : candidates) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", c.getId());
                item.put("昵称", c.getUsername());
                item.put("性别", "b".equals(c.getGender()) ? "男" : "女");
                item.put("标签", c.getTags().stream().map(Tag::getTag).collect(Collectors.toList()));
                candidateList.add(item);
            }

            return "根据当前用户和候选用户的标签，推荐最匹配的3-5位伙伴，并为每位生成一句推荐理由。\n\n"
                    + "【当前用户】\n"
                    + objectMapper.writeValueAsString(currentUserJson) + "\n\n"
                    + "【候选用户】\n"
                    + objectMapper.writeValueAsString(candidateList) + "\n\n"
                    + "【返回格式】严格按以下 JSON 返回，不要包含 markdown 代码块标记：\n"
                    + "[{\"id\": 用户ID, \"reason\": \"推荐理由\"}]";
        } catch (Exception e) {
            throw new RuntimeException("构建提示词失败", e);
        }
    }

}
