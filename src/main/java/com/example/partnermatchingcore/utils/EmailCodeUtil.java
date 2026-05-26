package com.example.partnermatchingcore.utils;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class EmailCodeUtil {
    private final Map<String, String> codeMap = new HashMap<>();

    // 生成6位验证码
    public String generateCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    // 保存验证码
    public void saveCode(String email, String code) {
        codeMap.put(email, code);
    }

    // 校验验证码
    public boolean verify(String email, String code) {
        return code.equals(codeMap.get(email));
    }
}
