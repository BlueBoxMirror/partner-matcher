package cxlab.partnermatcher.controller;

import cxlab.partnermatcher.pojo.UpdateUserRequest;
import cxlab.partnermatcher.pojo.UserV0;
import cxlab.partnermatcher.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public UserV0 getProfile(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return userService.getUserProfile(userId);
    }

    @PutMapping("/profile")
    public void updateProfile(@RequestBody UpdateUserRequest requestBody,
                              HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        userService.updateUserProfile(userId, requestBody);
    }

    @GetMapping("/tags/all")
    public List<String> getAllTags() {
        return userService.getAllTags();
    }

    //临时方案：返回固定测试用户ID（假设数据库中有id=1的用户）
    //等认证模块完成后，替换为从 request 中获取真实 userId
    private Long getCurrentUserId(HttpServletRequest request) {
        return 1L;
    }
}