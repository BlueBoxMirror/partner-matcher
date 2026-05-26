package com.partner.partnermatch;

import com.partner.partnermatch.entity.ai.AITag;
import com.partner.partnermatch.entity.ai.AIUserTag;
import com.partner.partnermatch.mapper.TagMapper;
import com.partner.partnermatch.mapper.UserMapper;
import com.partner.partnermatch.mapper.UserTagMapper;
import com.partner.partnermatch.service.LuceneStorageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;
//创建测试数据
@Slf4j
@SpringBootTest
public class SeedDataTest {

    @Resource
    private TagMapper tagMapper;

    @Resource
    private UserTagMapper userTagMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private LuceneStorageService luceneStorageService;

    private static final String[][] TAG_DATA = {
            {"Java", "技术"}, {"Python", "技术"}, {"C语言", "技术"}, {"前端", "技术"}, {"后端", "技术"},
            {"算法", "技术"}, {"AI", "技术"}, {"数据库", "技术"}, {"Linux", "技术"}, {"嵌入式", "技术"},
            {"篮球", "运动"}, {"足球", "运动"}, {"羽毛球", "运动"}, {"乒乓球", "运动"}, {"跑步", "运动"},
            {"游泳", "运动"}, {"健身", "运动"}, {"骑行", "运动"}, {"滑雪", "运动"}, {"网球", "运动"},
            {"绘画", "艺术"}, {"摄影", "艺术"}, {"音乐", "艺术"}, {"舞蹈", "艺术"}, {"书法", "艺术"},
            {"吉他", "艺术"}, {"钢琴", "艺术"}, {"设计", "艺术"}, {"剪辑", "艺术"}, {"写作", "艺术"},
            {"数学", "学习"}, {"英语", "学习"}, {"考研", "学习"}, {"考公", "学习"}, {"竞赛", "学习"},
            {"阅读", "学习"}, {"留学", "学习"}, {"考证", "学习"}, {"科研", "学习"}, {"托福", "学习"},
            {"游戏", "娱乐"}, {"动漫", "娱乐"}, {"电影", "娱乐"}, {"追剧", "娱乐"}, {"K歌", "娱乐"},
            {"桌游", "娱乐"}, {"旅行", "娱乐"}, {"美食", "娱乐"}, {"汉服", "娱乐"}, {"二次元", "娱乐"},
    };

    @Test
    void seedTagsAndUpdateIndex() throws Exception {
        // 1. 清空旧数据
        userTagMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>());
        tagMapper.delete(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>());
        log.info("已清空 tags 和 user_tag 表");

        // 2. 插入 50 个标签
        List<AITag> allTags = new ArrayList<>();
        for (String[] row : TAG_DATA) {
            AITag tag = new AITag();
            tag.setTagName(row[0]);
            tag.setTagType(row[1]);
            tagMapper.insert(tag);
            allTags.add(tag);
        }
        log.info("已插入 {} 个标签", allTags.size());

        // 3. 获取所有用户
        List<Long> userIds = userMapper.selectList(null).stream()
                .map(com.partner.partnermatch.entity.ai.AIUser::getId)
                .collect(Collectors.toList());
        if (userIds.isEmpty()) {
            log.warn("没有用户，跳过 user_tag 插入");
            return;
        }
        log.info("共 {} 个用户", userIds.size());

        // 4. 为每个用户随机分配 3 个标签
        Random rand = new Random();
        List<Integer> tagIds = allTags.stream().map(AITag::getId).collect(Collectors.toList());

        for (Long userId : userIds) {
            Collections.shuffle(tagIds, rand);
            for (int i = 0; i < 3; i++) {
                AIUserTag ut = new AIUserTag();
                ut.setUserId(userId);
                ut.setTagId(tagIds.get(i));
                userTagMapper.insert(ut);
            }
        }
        log.info("已为 {} 个用户分配标签", userIds.size());

        // 5. 重建 Lucene 向量索引
        for (Long userId : userIds) {
            List<Map<String, Object>> rows = userTagMapper.findTagsByUserIds(List.of(userId));
            String[] tagNames = rows.stream()
                    .map(r -> (String) r.get("tag"))
                    .toArray(String[]::new);
            luceneStorageService.updateUserTags(userId, tagNames);
        }
        log.info("RAG 向量索引已重建，共 {} 个用户", userIds.size());
    }
}
