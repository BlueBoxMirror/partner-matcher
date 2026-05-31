package cxlab.partnermatcher.service.impl;

import cxlab.partnermatcher.mapper.UserMapper;
import cxlab.partnermatcher.pojo.User;
import cxlab.partnermatcher.pojo.dto.UserLoginRequest;
import cxlab.partnermatcher.pojo.dto.UserLoginResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    @Override
    public UserLoginResponse login(UserLoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String pwd = loginRequest.getPassword();

        if (username == null || username.trim().isEmpty() || pwd == null || pwd.trim().isEmpty()) {
            throw new RuntimeException("账号或密码不能为空");
        }

        User user = userMapper.selectByUsernameOrEmail(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] inputPwdBytes = sha256.digest(pwd.getBytes(StandardCharsets.UTF_8));

            if (!MessageDigest.isEqual(inputPwdBytes, user.getPassword())) {
                throw new RuntimeException("密码错误");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("密码校验异常");
        }

        String token = UUID.randomUUID().toString();
        return new UserLoginResponse(token, user.getId(), user.getUsername(), user.getQqEmail());
    }

    @Override
    @Transactional
    public User register(User user) {
        // 1. 参数校验
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()
                || user.getPwd() == null || user.getPwd().trim().isEmpty()) {
            throw new RuntimeException("用户名和密码不能为空");
        }

        User existUser = userMapper.selectByUsernameOrEmail(user.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已被注册");
        }

        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] encryptedPwd = sha256.digest(user.getPwd().getBytes(StandardCharsets.UTF_8));
            user.setPassword(encryptedPwd);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("密码加密失败");
        }

        if (user.getQqEmail() == null) {
            user.setQqEmail("");
        }
        if (user.getGender() == null) {
            user.setGender(0);
        }
        if (user.getCollectNumber() == null) {
            user.setCollectNumber(0);
        }
        if (user.getTags() == null) {
            user.setTags("[]");
        }

        userMapper.insert(user);
        user.setPwd(null);
        return user;
    }
}