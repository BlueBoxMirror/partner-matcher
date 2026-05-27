package cxlab.partnermatcher.controller;

import cxlab.partnermatcher.pojo.dto.UserLoginRequest;
import cxlab.partnermatcher.pojo.dto.UserLoginResponse;
import cxlab.partnermatcher.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Object login(@RequestBody UserLoginRequest loginRequest) {
        try {
            UserLoginResponse resp = userService.login(loginRequest);
            return resp;
        } catch (RuntimeException e) {
            return "登录失败：" + e.getMessage();
        }
    }
}