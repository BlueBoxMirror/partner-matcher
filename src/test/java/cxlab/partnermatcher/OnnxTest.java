package cxlab.partnermatcher;

import ai.onnxruntime.OrtException;
import cxlab.partnermatcher.service.impl.RAGTransferService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
@Slf4j
public class OnnxTest {
    @Autowired private RAGTransferService ragTransferService;
    @Test
    public void testOnnx() throws OrtException {
        String[] texts = new String[]{
            "苹果很好吃", "手机很好玩", "香蕉尝起来不错", "菠萝是美味的", "Java语言开发", "Python编程入门"
        };
        float[][] embeddings = ragTransferService.encode(texts);
        for (int i = 0; i < embeddings.length; i++) {
            for(int j = i+1; j < embeddings.length; j++){
                float sim = ragTransferService.dot(embeddings[i], embeddings[j]);
                if(sim>0.6){
                    log.info("{} 和 {} 接近: {}", texts[i], texts[j], sim);
                }
            }
        }

    }
}
