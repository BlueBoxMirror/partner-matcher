package com.example.demo.service;

import com.example.demo.pojo.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.PasswordUtil.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        byte[] hashedInput = PasswordUtil.sha256(password);
        if (!Arrays.equals(hashedInput, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        return user;
    }
}