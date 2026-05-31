package com.example.demo.controller;
import com.example.demo.pojo.JwtUtil;
import com.example.demo.pojo.Result;
import com.example.demo.DTO.LoginRequest;
import com.example.demo.pojo.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.login(request.getUsername(), request.getPassword());
        String token = jwtUtil.generateToken(user.getId());
        return Result.success(token);
    }
}