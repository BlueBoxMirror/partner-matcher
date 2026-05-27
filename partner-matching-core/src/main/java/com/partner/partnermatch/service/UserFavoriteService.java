package com.partner.partnermatch.service;

import com.partner.partnermatch.dto.FavoriteUserDto;
import java.util.List;

public interface UserFavoriteService {
    void toggleFavorite(Long userId, Long collectUserId);
    List<FavoriteUserDto> getFavoriteList(Long userId);
}