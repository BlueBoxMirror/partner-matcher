package com.example.demo.PasswordUtil;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class PasswordUtil {
    public static byte[] sha256(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(plainText.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}