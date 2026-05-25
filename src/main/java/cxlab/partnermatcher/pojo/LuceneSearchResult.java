package cxlab.partnermatcher.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

@Data
@AllArgsConstructor
public class LuceneSearchResult {
    private long[] userIds;
    private ScoreDoc lastScoreDoc;
}
