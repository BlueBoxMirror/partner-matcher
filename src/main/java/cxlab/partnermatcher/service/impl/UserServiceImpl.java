package cxlab.partnermatcher.service.impl;

import cxlab.partnermatcher.mapper.UserMapper;
import cxlab.partnermatcher.pojo.User;
import cxlab.partnermatcher.pojo.dto.UserLoginRequest;
import cxlab.partnermatcher.pojo.dto.UserLoginResponse;
import cxlab.partnermatcher.service.UserService;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserLoginResponse login(UserLoginRequest loginRequest) {
        String account = loginRequest.getAccount();
        String pwd = loginRequest.getPassword();
        if (account == null || account.trim().isEmpty() || pwd == null || pwd.trim().isEmpty()) {
            throw new RuntimeException("账号或密码不能为空");
        }

        User user = userMapper.selectByAccountOrEmail(account);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] inputPwdBytes = sha256.digest(pwd.getBytes());
            if (!Arrays.equals(inputPwdBytes, user.getPassword())) {
                throw new RuntimeException("密码错误");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("密码校验异常");
        }

        String token = UUID.randomUUID().toString();
        return new UserLoginResponse(token, user.getId(), user.getUsername(), user.getQqEmail());
    }
}