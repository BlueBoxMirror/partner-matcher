package com.partner.partnermatch.service;

import ai.onnxruntime.OrtException;
import com.partner.partnermatch.pojo.LuceneSearchResult;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.util.List;

public interface TagService {
    void updateTags(Long userId, List<Integer> tagIds) throws OrtException, IOException;
    LuceneSearchResult searchByTag(String tag, int count) throws IOException;
    LuceneSearchResult searchByTag(String tag, int count, ScoreDoc lastScoreDoc) throws IOException;

    LuceneSearchResult searchByEmbedding(String[] tags, int count) throws IOException, OrtException;
    LuceneSearchResult searchByEmbedding(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException, OrtException;
    LuceneSearchResult searchExactByTags(String[] tags, int count) throws IOException;
    LuceneSearchResult searchExactByTags(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException;
    LuceneSearchResult searchFuzzyByTags(String[] tags, int count) throws IOException;
    LuceneSearchResult searchFuzzyByTags(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException;
}
