package com.partner.partnermatch.controller;

import com.partner.partnermatch.common.Result;
import com.partner.partnermatch.dto.FavoriteToggleRequest;
import com.partner.partnermatch.dto.FavoriteUserDto;
import com.partner.partnermatch.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user/favorite")
public class UserFavoriteController {

    @Autowired
    private UserFavoriteService userFavoriteService;

    @PostMapping("/toggle")
    public Result<Void> toggleFavorite(@RequestBody FavoriteToggleRequest request) {
        userFavoriteService.toggleFavorite(request.getUserId(), request.getCollectUserId());
        return Result.<Void>success(null);
    }

    @GetMapping("/list")
    public Result<List<FavoriteUserDto>> getFavoriteList(@RequestParam Long userId) {
        List<FavoriteUserDto> list = userFavoriteService.getFavoriteList(userId);
        return Result.success(list);
    }
}