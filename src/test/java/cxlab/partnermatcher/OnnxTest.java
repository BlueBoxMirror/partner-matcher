package cxlab.partnermatcher;

import ai.onnxruntime.OrtException;
import cxlab.partnermatcher.service.impl.RAGTransferService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class OnnxTest {
    @Autowired private RAGTransferService ragTransferService;
    @Test
    public void simpleTest() throws OrtException {
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
}
