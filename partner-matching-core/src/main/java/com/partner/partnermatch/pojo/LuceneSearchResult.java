package com.partner.partnermatch.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.lucene.search.ScoreDoc;

import java.util.Map;

@Data
@AllArgsConstructor
public class LuceneSearchResult {
    private long[] userIds;
    private Map<Long,ScoreDoc> scoreDocs;
    private ScoreDoc lastScoreDoc;

    public ScoreDoc getScoreDocByUserId(long userId){
        return scoreDocs.get(userId);
    }
}
