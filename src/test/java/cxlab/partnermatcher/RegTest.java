package cxlab.partnermatcher;

import ai.onnxruntime.OrtException;
import cxlab.partnermatcher.service.impl.LuceneStorageService;
import cxlab.partnermatcher.service.impl.RAGTransferService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
@Slf4j
public class RegTest {
    @Autowired private RAGTransferService ragTransferService;
    @Autowired private LuceneStorageService luceneStorageService;
    @Test
    public void KnnTest() throws OrtException {
        String[] texts = new String[]{
                "水果", "香蕉", "玻璃", "材料"
        };
        float[][] embeddings = ragTransferService.encode(texts);
        for(int i=0;i<embeddings.length;i++){
            for(int j=i+1;j<embeddings.length;j++){
                log.info("\"{}\"和\"{}\"的相似度为:{}",texts[i],texts[j],ragTransferService.dot(embeddings[i],embeddings[j]));
            }
        }
    }

    @Test
    public void luceneWriteTest() throws OrtException, IOException {
        luceneStorageService.updateUserTags(1, new String[]{"游戏", "二次元"});
        luceneStorageService.updateUserTags(2, new String[]{"音乐", "绘画", "游戏"});
        luceneStorageService.updateUserTags(3, new String[]{"游戏", "二次元", "绘画"});
    }
    @Test
    public void luceneReadTest() throws IOException, OrtException {
        log.info("{}",luceneStorageService.searchByTag("二次元", 10));
        log.info("{}",luceneStorageService.searchExactByTags(new String[]{"游戏","二次元"}, 10));
        log.info("{}",luceneStorageService.searchFuzzyByTags(new String[]{"游戏","二次元","绘画"}, 10));
        log.info("{}",luceneStorageService.searchByEmbedding(ragTransferService.encode("游戏二次元"), 10));
    }
}
