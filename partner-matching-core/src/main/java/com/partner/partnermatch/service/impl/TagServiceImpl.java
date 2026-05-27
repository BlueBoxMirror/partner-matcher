package com.partner.partnermatch.service.impl;

import ai.onnxruntime.OrtException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.partner.partnermatch.entity.ai.AITag;
import com.partner.partnermatch.entity.ai.AIUser;
import com.partner.partnermatch.entity.ai.AIUserTag;
import com.partner.partnermatch.event.TagChangedEvent;
import com.partner.partnermatch.mapper.TagMapper;
import com.partner.partnermatch.mapper.UserMapper;
import com.partner.partnermatch.mapper.UserTagMapper;
import com.partner.partnermatch.pojo.LuceneSearchResult;
import com.partner.partnermatch.pojo.vo.LuceneSearchVO;
import com.partner.partnermatch.pojo.vo.TagVO;
import com.partner.partnermatch.pojo.vo.UserVO;
import com.partner.partnermatch.service.LuceneStorageService;
import com.partner.partnermatch.service.RAGTransferService;
import com.partner.partnermatch.service.TagService;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private UserTagMapper userTagMapper;

    @Autowired private UserMapper userMapper;

    @Autowired private TagMapper tagMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired private LuceneStorageService luceneStorageService;
    @Autowired private RAGTransferService ragTransferService;
    @Autowired
    private ObjectMapper objectMapper;

    private LuceneSearchVO toVO(LuceneSearchResult result){
        ArrayList<Long> ids = new ArrayList<>();
        for (long uid : result.getUserIds()) ids.add(uid);
        return new LuceneSearchVO(userMapper.selectByIds(ids).stream().map(UserVO::new).toList(), result.getLastScoreDoc());
    }

    @Override
    public LuceneSearchVO searchByTag(String tag, int count) throws IOException {
        return toVO(luceneStorageService.searchByTag(tag, count));
    }

    @Override
    public LuceneSearchVO searchByTag(String tag, int count, ScoreDoc lastScoreDoc) throws IOException {
        return toVO(luceneStorageService.searchByTag(tag, count, lastScoreDoc));
    }

    @Override
    public LuceneSearchVO searchByEmbedding(String[] tags, int count) throws IOException, OrtException {
        StringBuilder builder = new StringBuilder();
        for (String tag : tags) {
            builder.append(tag).append(" ");
        }
        float[] embedding = ragTransferService.encode(builder.toString());
        return toVO(luceneStorageService.searchByEmbedding(embedding, count));
    }

    @Override
    public LuceneSearchVO searchByEmbedding(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException, OrtException {
        StringBuilder builder = new StringBuilder();
        for (String tag : tags) {
            builder.append(tag).append(" ");
        }
        float[] embedding = ragTransferService.encode(builder.toString());
        return toVO(luceneStorageService.searchByEmbedding(embedding, count, lastScoreDoc));
    }

    @Override
    public LuceneSearchVO searchExactByTags(String[] tags, int count) throws IOException {
        return toVO(luceneStorageService.searchExactByTags(tags, count));
    }

    @Override
    public LuceneSearchVO searchExactByTags(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException {
        return toVO(luceneStorageService.searchExactByTags(tags, count, lastScoreDoc));
    }

    @Override
    public LuceneSearchVO searchFuzzyByTags(String[] tags, int count) throws IOException {
        return toVO(luceneStorageService.searchFuzzyByTags(tags, count));
    }

    @Override
    public LuceneSearchVO searchFuzzyByTags(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException {
        return toVO(luceneStorageService.searchFuzzyByTags(tags, count, lastScoreDoc));
    }


    @Override
    public LuceneSearchVO recommend(long id, int pageNum, int pageSize) {
        try {
            List<Map<String, Object>> tagMaps = userTagMapper.findTagsByUserIds(List.of(id));
            StringBuilder builder = new StringBuilder();
            for (Map<String, Object> row : tagMaps) {
                builder.append(row.get("tag")).append(" ");
            }
            float[] embedding = ragTransferService.encode(builder.toString());
            LuceneSearchResult result = null;
            if(pageNum==0) {
                result = luceneStorageService.searchByEmbedding(embedding, pageNum);
            }
            else{
                result = luceneStorageService.searchByEmbedding(embedding, pageNum*pageSize);
                result = luceneStorageService.searchByEmbedding(embedding, pageNum*pageSize, result.getLastScoreDoc());
            }
            List<Long> ids = new ArrayList<>();
            for (long uid : result.getUserIds()) ids.add(uid);
            List<UserVO> users=userMapper.selectBatchIds(ids).stream().map(UserVO::new).toList();
            return new LuceneSearchVO(users, result.getLastScoreDoc());
        } catch (OrtException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public void updateTags(Long userId, List<Integer> _tagIds) throws IOException, OrtException {
        List<Integer> tagIds = new HashSet<>(_tagIds).stream().toList();
        //连表
        userTagMapper.delete(new QueryWrapper<AIUserTag>().eq("user_id", userId));

        for (Integer tagId : tagIds) {
            AIUserTag ut = new AIUserTag();
            ut.setUserId(userId);
            ut.setTagId(tagId);
            userTagMapper.insert(ut);
        }

        List<String> tags = tagIds.isEmpty() ? List.of() : tagMapper.selectByIds(tagIds).stream().map(AITag::getTagName).toList();
        //用户表
        userMapper.update(new UpdateWrapper<AIUser>().eq("id", userId).set("tags",objectMapper.writeValueAsString(tags)));
        //lucene
        luceneStorageService.updateUserTags(userId, tags.toArray(String[]::new));


        //触发缓存更新
        eventPublisher.publishEvent(new TagChangedEvent(this, userId));
    }

    @Override
    public List<TagVO> getAllTags() {
        return tagMapper.selectList(new QueryWrapper<AITag>().select("id", "tag_name", "tag_type")).stream().map(TagVO::new).toList();
    }

    @Override
    public String getTagNameById(Integer tagId) {
        return tagMapper.selectById(tagId).getTagName();
    }
}
