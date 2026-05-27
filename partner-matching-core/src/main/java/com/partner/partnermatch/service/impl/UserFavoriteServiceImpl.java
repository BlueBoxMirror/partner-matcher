package com.partner.partnermatch.service.impl;

import com.partner.partnermatch.dto.FavoriteUserDto;
import com.partner.partnermatch.entity.UserFavorite;
import com.partner.partnermatch.mapper.UserFavoriteMapper;
import com.partner.partnermatch.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class UserFavoriteServiceImpl implements UserFavoriteService {

    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

    @Override
    public void toggleFavorite(Long userId, Long collectUserId) {
        int count = userFavoriteMapper.selectCount(userId, collectUserId);
        if (count > 0) {
            userFavoriteMapper.deleteFavorite(userId, collectUserId);
        } else {
            UserFavorite favorite = new UserFavorite();
            favorite.setUserId(userId);
            favorite.setCollectUserId(collectUserId);
            favorite.setCreatedAt(new Date());
            userFavoriteMapper.insertFavorite(favorite);
        }
    }

    @Override
    public List<FavoriteUserDto> getFavoriteList(Long userId) {
        return userFavoriteMapper.selectFavoriteList(userId);
    }
}