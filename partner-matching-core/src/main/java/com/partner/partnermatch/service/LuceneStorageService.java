package com.partner.partnermatch.service;

import ai.onnxruntime.OrtException;
import com.partner.partnermatch.pojo.LuceneSearchResult;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.*;

import java.io.IOException;

public interface LuceneStorageService {
    Document searchById(long userId) throws IOException;

    void updateUserTags(long userId, String[] tags) throws OrtException, IOException;
    void deleteUser(long userId) throws IOException;
    LuceneSearchResult searchByTag(String tag, int count) throws IOException;
    LuceneSearchResult searchByTag(String tag, int count, ScoreDoc lastScoreDoc) throws IOException;

    LuceneSearchResult searchByEmbedding(float[] embedding, int count) throws IOException;
    LuceneSearchResult searchByEmbedding(float[] embedding, int count, ScoreDoc lastScoreDoc) throws IOException;
    LuceneSearchResult searchExactByTags(String[] tags, int count) throws IOException;
    LuceneSearchResult searchExactByTags(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException;
    LuceneSearchResult searchFuzzyByTags(String[] tags, int count) throws IOException;
    LuceneSearchResult searchFuzzyByTags(String[] tags, int count, ScoreDoc lastScoreDoc) throws IOException;
}
