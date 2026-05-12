package cxlab.partnermatcher.service.impl;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

@Service
@Slf4j
public class RAGTransferService {
    private OrtEnvironment ortEnv = OrtEnvironment.getEnvironment();
    @Getter
    private OrtSession session = ortEnv.createSession("model.onnx");

    private HuggingFaceTokenizer tokenizer = HuggingFaceTokenizer.newInstance(Paths.get(".","tokenizer.json"));

    public RAGTransferService() throws OrtException, IOException {
    }
    public float[] encode(String text) throws OrtException {
        return encode(new String[]{text})[0];
    }

    public float[][] encode(String[] texts) throws OrtException {
        long[][] inputIdsList = new long[texts.length][];
        long[][] attentionMaskList = new long[texts.length][];
        long[][] typeIdsList = new long[texts.length][];
        Encoding[] encodings = tokenizer.batchEncode(texts);
        for (int i = 0; i < texts.length; i++) {
            Encoding encoding = encodings[i];
            inputIdsList[i] = encoding.getIds();
            attentionMaskList[i] = encoding.getAttentionMask();
            typeIdsList[i] = encoding.getTypeIds();
        }

        OnnxTensor inputIdsTensor = OnnxTensor.createTensor(ortEnv, inputIdsList);
        OnnxTensor attentionMaskTensor = OnnxTensor.createTensor(ortEnv, attentionMaskList);
        OnnxTensor tokenTypeIdsTensor = OnnxTensor.createTensor(ortEnv, typeIdsList);

        HashMap<String, OnnxTensor> inputs = new HashMap<>();
        inputs.put("input_ids", inputIdsTensor);
        inputs.put("attention_mask", attentionMaskTensor);
        inputs.put("token_type_ids", tokenTypeIdsTensor);

        try(OrtSession.Result result = session.run(inputs)){
            OnnxTensor output = (OnnxTensor) result.get("sentence_embedding").get();
            return (float[][]) output.getValue();
        }
        finally {
            inputIdsTensor.close();
            attentionMaskTensor.close();
            tokenTypeIdsTensor.close();
        }
    }

    public float dot(float[] a, float[] b){
        float sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * b[i];
        }
        return sum;
    }

}
