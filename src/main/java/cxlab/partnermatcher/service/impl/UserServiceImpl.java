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

    // 登录逻辑（完全不变，和之前一模一样）
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
            // 和注册完全一致的加密逻辑
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

    // 注册逻辑（只改了3行，其他完全不变）
    @Override
    @Transactional
    public User register(User user) {
        // 1. 参数校验
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()
                || user.getPwd() == null || user.getPwd().trim().isEmpty()) {
            throw new RuntimeException("用户名和密码不能为空");
        }

        // 2. 检查用户名是否已存在
        User existUser = userMapper.selectByUsernameOrEmail(user.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已被注册");
        }

        // 3. 密码加密（和登录逻辑完全一致）
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] encryptedPwd = sha256.digest(user.getPwd().getBytes(StandardCharsets.UTF_8));
            user.setPassword(encryptedPwd); // 存到数据库的password字段，完全不变
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("密码加密失败");
        }

        // 4. 补充默认值
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

        // 5. 插入数据库
        userMapper.insert(user);
        // 6. 清空明文密码再返回（安全）
        user.setPwd(null);
        return user;
    }
}