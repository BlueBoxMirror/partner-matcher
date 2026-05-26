package com.partner.partnermatch.service.impl;

import ai.onnxruntime.OrtException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.partner.partnermatch.entity.ai.AITag;
import com.partner.partnermatch.entity.ai.AIUser;
import com.partner.partnermatch.entity.ai.AIUserTag;
import com.partner.partnermatch.event.TagChangedEvent;
import com.partner.partnermatch.mapper.TagMapper;
import com.partner.partnermatch.mapper.UserMapper;
import com.partner.partnermatch.mapper.UserTagMapper;
import com.partner.partnermatch.pojo.LuceneSearchResult;
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
import java.util.List;
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

    @Override
    public LuceneSearchResult searchByTag(String tag, int count) throws IOException {
        return luceneStorageService.searchByTag(tag, count);
    }

    @Override
    public LuceneSearchResult searchByTag(String tag, int count, ScoreDoc lastScoreDoc) throws IOException {
        return luceneStorageService.searchByTag(tag, count, lastScoreDoc);
    }

    @Override
    public LuceneSearchResult searchByEmbedding(String[] tags, int count) throws IOException, OrtException {
        StringBuilder builder = new StringBuilder();
        for (String tag : tags) {
            builder.append(tag).append(" ");
        }
        float[] embedding = ragTransferService.encode(builder.toString());
        return luceneStorageService.searchByEmbedding(embedding, count);
    }

    @Override
    public LuceneSearchResult searchByEmbedding(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException, OrtException {
        StringBuilder builder = new StringBuilder();
        for (String tag : tags) {
            builder.append(tag).append(" ");
        }
        float[] embedding = ragTransferService.encode(builder.toString());
        return luceneStorageService.searchByEmbedding(embedding, count, lastScoreDoc);
    }

    @Override
    public LuceneSearchResult searchExactByTags(String[] tags, int count) throws IOException {
        return luceneStorageService.searchExactByTags(tags, count);
    }

    @Override
    public LuceneSearchResult searchExactByTags(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException {
        return luceneStorageService.searchExactByTags(tags, count, lastScoreDoc);
    }

    @Override
    public LuceneSearchResult searchFuzzyByTags(String[] tags, int count) throws IOException {
        return luceneStorageService.searchFuzzyByTags(tags, count);
    }

    @Override
    public LuceneSearchResult searchFuzzyByTags(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException {
        return luceneStorageService.searchFuzzyByTags(tags, count, lastScoreDoc);
    }


    @Override
    @Transactional
    public void updateTags(Long userId, List<Integer> tagIds) throws IOException, OrtException {
        //连表
        userTagMapper.delete(new QueryWrapper<AIUserTag>().eq("user_id", userId));

        for (Integer tagId : tagIds) {
            AIUserTag ut = new AIUserTag();
            ut.setUserId(userId);
            ut.setTagId(tagId);
            userTagMapper.insert(ut);
        }
        //用户表
        List<AITag> tags = tagMapper.selectByIds(tagIds).stream().toList();
        userMapper.update(new UpdateWrapper<AIUser>().eq("id", userId).set("tags", tags));
        //lucene
        luceneStorageService.updateUserTags(userId, tags.stream().map(AITag::getTagName).toArray(String[]::new));


        //触发缓存更新
        eventPublisher.publishEvent(new TagChangedEvent(this, userId));
    }
}
