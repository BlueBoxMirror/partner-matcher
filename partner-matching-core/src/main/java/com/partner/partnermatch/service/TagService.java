package com.partner.partnermatch.service;

import ai.onnxruntime.OrtException;
import com.partner.partnermatch.pojo.LuceneSearchResult;
import com.partner.partnermatch.pojo.vo.TagVO;
import com.partner.partnermatch.pojo.vo.UserVO;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.util.List;

public interface TagService {
    List<UserVO> recommend(long id, int pageNum, int pageSize);
    void updateTags(Long userId, List<Integer> tagIds) throws OrtException, IOException;
    String getTagNameById(Integer tagId);
    LuceneSearchResult searchByTag(String tag, int count) throws IOException;
    LuceneSearchResult searchByTag(String tag, int count, ScoreDoc lastScoreDoc) throws IOException;

    LuceneSearchResult searchByEmbedding(String[] tags, int count) throws IOException, OrtException;
    LuceneSearchResult searchByEmbedding(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException, OrtException;
    LuceneSearchResult searchExactByTags(String[] tags, int count) throws IOException;
    LuceneSearchResult searchExactByTags(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException;
    LuceneSearchResult searchFuzzyByTags(String[] tags, int count) throws IOException;
    LuceneSearchResult searchFuzzyByTags(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException;

    List<TagVO> getAllTags();
}
