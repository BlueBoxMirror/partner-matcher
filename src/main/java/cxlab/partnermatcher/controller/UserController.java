package cxlab.partnermatcher.controller;

import cxlab.partnermatcher.common.Result;
import cxlab.partnermatcher.pojo.User;
import cxlab.partnermatcher.pojo.dto.UserLoginRequest;
import cxlab.partnermatcher.pojo.dto.UserLoginResponse;
import cxlab.partnermatcher.service.impl.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 登录接口
    @PostMapping("/login")
    public Result<UserLoginResponse> login(@RequestBody UserLoginRequest loginRequest) {
        try {
            UserLoginResponse response = userService.login(loginRequest);
            return Result.success(response);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        try {
            User registeredUser = userService.register(user);
            return Result.success(registeredUser);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PostMapping("/emailLogin")
    public Result<String> emailLogin() {
        return Result.success("邮箱登录接口已就绪");
    }

    @PostMapping("/sendCode")
    public Result<String> sendCode() {
        return Result.success("发送验证码接口已就绪");
    }
}