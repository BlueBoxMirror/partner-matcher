package com.partner.partnermatch.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.lucene.search.ScoreDoc;

import java.util.List;

@Data
@AllArgsConstructor
public class LuceneSearchVO {
    private List<UserVO> users;
    private ScoreDoc lastScoreDoc;
}
