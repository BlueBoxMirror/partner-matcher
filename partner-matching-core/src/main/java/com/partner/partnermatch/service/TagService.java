package com.partner.partnermatch.service;

import ai.onnxruntime.OrtException;
import com.partner.partnermatch.pojo.LuceneSearchResult;
import com.partner.partnermatch.pojo.vo.LuceneSearchVO;
import com.partner.partnermatch.pojo.vo.TagVO;
import com.partner.partnermatch.pojo.vo.UserVO;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.util.List;

public interface TagService {
    LuceneSearchVO recommend(long id, int pageNum, int pageSize);
    void updateTags(Long userId, List<Integer> tagIds) throws OrtException, IOException;
    String getTagNameById(Integer tagId);
    LuceneSearchVO searchByTag(String tag, int count) throws IOException;
    LuceneSearchVO searchByTag(String tag, int count, ScoreDoc lastScoreDoc) throws IOException;

    LuceneSearchVO searchByEmbedding(String[] tags, int count) throws IOException, OrtException;
    LuceneSearchVO searchByEmbedding(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException, OrtException;
    LuceneSearchVO searchExactByTags(String[] tags, int count) throws IOException;
    LuceneSearchVO searchExactByTags(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException;
    LuceneSearchVO searchFuzzyByTags(String[] tags, int count) throws IOException;
    LuceneSearchVO searchFuzzyByTags(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException;

    List<TagVO> getAllTags();
}
