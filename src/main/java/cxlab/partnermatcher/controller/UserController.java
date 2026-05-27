package cxlab.partnermatcher.controller;

import cxlab.partnermatcher.common.Result;
import cxlab.partnermatcher.pojo.dto.UserLoginRequest;
import cxlab.partnermatcher.pojo.dto.UserLoginResponse;
import cxlab.partnermatcher.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// 关键：和前端约定的接口前缀完全一致：/api/user
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. 账号密码登录：/api/user/login（POST，和前端约定一致）
    @PostMapping("/login")
    public Result<UserLoginResponse> login(@RequestBody UserLoginRequest loginRequest) {
        try {
            UserLoginResponse response = userService.login(loginRequest);
            return Result.success(response);
        } catch (RuntimeException e) {
            return Result.error(500, e.getMessage());
        }
    }

    // 2. 账号密码注册：/api/user/register（POST，占位）
    @PostMapping("/register")
    public Result<String> register() {
        return Result.success("注册接口已就绪");
    }

    // 3. 邮箱验证码登录：/api/user/emailLogin（POST，占位）
    @PostMapping("/emailLogin")
    public Result<String> emailLogin() {
        return Result.success("邮箱登录接口已就绪");
    }

    // 4. 发送邮箱验证码：/api/user/sendCode（POST，占位）
    @PostMapping("/sendCode")
    public Result<String> sendCode() {
        return Result.success("发送验证码接口已就绪");
    }
}