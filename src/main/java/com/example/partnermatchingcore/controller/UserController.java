package com.example.partnermatchingcore.controller;

import com.example.partnermatchingcore.common.R;
import com.example.partnermatchingcore.entity.User;
import com.example.partnermatchingcore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 发送注册验证码
    @PostMapping("/send-code")
    public R<Void> sendCode(@RequestParam String email) {
        userService.sendCode(email);
        return R.ok(null);
    }

    // 用户注册（前端传的密码字段名改成 pwd，完全不冲突）
    @PostMapping("/register")
    public R<User> register(@RequestBody User user) {
        return R.ok(userService.register(user));
    }

    // 邮箱验证码登录
    @PostMapping("/login/email")
    public R<User> loginByEmail(
            @RequestParam String email,
            @RequestParam String code) {
        return R.ok(userService.loginByEmail(email, code));
    }
}