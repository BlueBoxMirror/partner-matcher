package com.example.partnermatchingcore.service;

import com.example.partnermatchingcore.entity.User;
import com.example.partnermatchingcore.mapper.UserMapper;
import com.example.partnermatchingcore.utils.EmailCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final JavaMailSender mailSender;
    private final EmailCodeUtil emailCodeUtil;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public void sendCode(String email) {
        if (!StringUtils.hasText(email) || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new RuntimeException("邮箱格式不正确");
        }
        String code = emailCodeUtil.generateCode();
        emailCodeUtil.saveCode(email, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("3068067142@qq.com");
        message.setTo(email);
        message.setSubject("注册验证码");
        message.setText("验证码：" + code + "，5分钟内有效");
        mailSender.send(message);
    }

    // 最终版注册，零报错、兼容BINARY(32)
    public User register(User user) {
        // 校验明文密码（现在是 String 类型的 pwd，不会再报错）
        if (!StringUtils.hasText(user.getQqEmail()) ||
                !StringUtils.hasText(user.getUsername()) ||
                !StringUtils.hasText(user.getPwd())) {
            throw new RuntimeException("邮箱、用户名、密码不能为空");
        }
        if (!EMAIL_PATTERN.matcher(user.getQqEmail()).matches()) {
            throw new RuntimeException("邮箱格式不正确");
        }
        User exist = userMapper.findByEmail(user.getQqEmail());
        if (exist != null) {
            throw new RuntimeException("该邮箱已注册");
        }

        // SHA256 加密 → 直接 byte[]，完美匹配 BINARY(32)
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encrypted = digest.digest(user.getPwd().getBytes(StandardCharsets.UTF_8));
            user.setPassword(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("密码加密失败");
        }

        userMapper.insert(user);
        return user;
    }

    public User loginByEmail(String email, String code) {
        if (!emailCodeUtil.verify(email, code)) {
            throw new RuntimeException("验证码错误或已过期");
        }
        return userMapper.findByEmail(email);
    }
}