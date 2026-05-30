package com.partner.partnermatch.service.impl;

import com.partner.partnermatch.dto.FavoriteUserDto;
import com.partner.partnermatch.mapper.UserFavoriteMapper;
import com.partner.partnermatch.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFavoriteServiceImpl implements UserFavoriteService {

    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

    @Override
    public void toggleFavorite(Long userId, Long collectUserId) {
        if (userId == null || collectUserId == null) {
            throw new RuntimeException("用户ID和被收藏用户ID不能为空");
    }
        int count = userFavoriteMapper.selectCount(userId, collectUserId);
        if (count > 0) {
            userFavoriteMapper.deleteFavorite(userId, collectUserId);
        } else {
            userFavoriteMapper.insertFavorite(userId, collectUserId);
        }
    }

    @Override
    public List<FavoriteUserDto> getFavoriteList(Long userId) {
        return userFavoriteMapper.selectFavoriteList(userId);
    }
}