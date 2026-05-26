package com.partner.partnermatch.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.lucene.search.ScoreDoc;

@Data
@AllArgsConstructor
public class LuceneSearchResult {
    private long[] userIds;
    private ScoreDoc lastScoreDoc;
}
