package com.partner.partnermatch.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.partner.partnermatch.common.Result;
import com.partner.partnermatch.dto.FavoritePageRequest;
import com.partner.partnermatch.dto.FavoriteToggleRequest;
import com.partner.partnermatch.dto.FavoriteUserDto;
import com.partner.partnermatch.service.UserFavoriteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/favorite")
public class UserFavoriteController {

    private final UserFavoriteService userFavoriteService;

    public UserFavoriteController(UserFavoriteService userFavoriteService) {
        this.userFavoriteService = userFavoriteService;
    }

    // 1. 收藏/取消收藏（一键切换）
    @PostMapping("/toggle")
    public Result<Boolean> toggle(@Validated @RequestBody FavoriteToggleRequest request,
                                  HttpServletRequest httpRequest) {
        // 后续替换为登录态获取userId，现在用固定值测试
        Long userId = 1L;
        boolean success = userFavoriteService.toggleCollection(userId, request.getCollectUserId());
        return Result.success(success);
    }

    // 2. 判断是否已收藏
    @GetMapping("/check")
    public Result<Boolean> checkCollected(@RequestParam Long collectUserId, HttpServletRequest httpRequest) {
        Long userId = 1L;
        boolean collected = userFavoriteService.isCollected(userId, Math.toIntExact(collectUserId));
        return Result.success(collected);
    }

    // 3. 获取收藏用户ID列表（兼容旧逻辑）
    @GetMapping("/listIds")
    public Result<List<Long>> listFavoriteIds(HttpServletRequest httpRequest) {
        Long userId = 1L;
        List<Long> ids = userFavoriteService.listFavoriteUserIds(userId);
        return Result.success(ids);
    }

    // 4. 单独取消收藏（列表页用）
    @PostMapping("/delete")
    public Result<Boolean> deleteFavorite(@Validated @RequestBody FavoriteToggleRequest request,
                                          HttpServletRequest httpRequest) {
        Long userId = 1L;
        boolean success = userFavoriteService.deleteFavorite(userId, request.getCollectUserId());
        return Result.success(success);
    }

    // 5. 分页查询收藏列表（按收藏时间倒序）
    @PostMapping("/list")
    public Result<Page<FavoriteUserDto>> listFavorites(@Validated @RequestBody FavoritePageRequest request,
                                                       HttpServletRequest httpRequest) {
        Long userId = 1L;
        Page<FavoriteUserDto> page = userFavoriteService.listFavoritesPage(userId, request);
        return Result.success(page);
    }
}