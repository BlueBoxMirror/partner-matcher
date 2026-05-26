package com.partner.partnermatch.controller;

import com.partner.partnermatch.common.Result;
import com.partner.partnermatch.dto.FavoriteToggleRequest;
import com.partner.partnermatch.service.UserFavoriteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/favorite")
public class UserFavoriteController {

    private final UserFavoriteService userFavoriteService;

    public UserFavoriteController(UserFavoriteService userFavoriteService) {
        this.userFavoriteService = userFavoriteService;
    }


      //切换收藏/取消收藏
     //POST /favorite/toggle
    @PostMapping("/toggle")
    public Result<Boolean> toggleFavorite(@RequestBody FavoriteToggleRequest request,
                                          HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getSession().getAttribute("loginUserId");
        boolean result = userFavoriteService.toggleCollection(userId, request.getCollectUserId());
        return Result.success(result);
    }

    //查询是否已收藏
     //GET /favorite/check?collectUserId=xxx

    @GetMapping("/check")
    public Result<Boolean> checkFavorite(@RequestParam Long collectUserId,
                                         HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("loginUserId");
        boolean isCollected = userFavoriteService.isCollected(userId, collectUserId);
        return Result.success(isCollected);
    }


     //获取收藏用户ID列表
     //GET /favorite/list

    @GetMapping("/list")
    public Result<List<Long>> listFavorite(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("loginUserId");
        List<Long> ids = userFavoriteService.listFavoriteUserIds(userId);
        return Result.success(ids);
    }


    @DeleteMapping("/delete")
    public Result<Boolean> deleteFavorite(@RequestParam Long collectUserId,
                                          HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("loginUserId");
        boolean result = userFavoriteService.deleteFavorite(userId, collectUserId);
        return Result.success(result);
    }
}