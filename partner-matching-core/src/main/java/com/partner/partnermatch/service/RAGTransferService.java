package com.partner.partnermatch.service;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtException;
import ai.onnxruntime.OrtSession;

import java.util.HashMap;

public interface RAGTransferService {
    float[] encode(String text) throws OrtException;

    float[][] encode(String[] texts) throws OrtException;

    float dot(float[] a, float[] b);

}
