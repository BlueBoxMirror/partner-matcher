package com.partner.partnermatch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.partner.partnermatch.entity.UserFavorite;
import com.partner.partnermatch.mapper.UserFavoriteMapper;
import com.partner.partnermatch.service.UserFavoriteService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserFavoriteServiceImpl extends ServiceImpl<UserFavoriteMapper, UserFavorite>
        implements UserFavoriteService {

    private final UserFavoriteMapper userFavoriteMapper;

    public UserFavoriteServiceImpl(UserFavoriteMapper userFavoriteMapper) {
        this.userFavoriteMapper = userFavoriteMapper;
    }

    @Override
    public boolean toggleCollection(Long userId, Long collectUserId) {
        boolean collected = isCollected(userId, collectUserId);
        if (collected) {
            lambdaUpdate()
                    .eq(UserFavorite::getUserId, userId)
                    .eq(UserFavorite::getCollectUserId, collectUserId)
                    .remove();
            return false;
        } else {
            UserFavorite favorite = new UserFavorite();
            favorite.setUserId(userId);
            favorite.setCollectUserId(collectUserId);
            save(favorite);
            return true;
        }
    }

    @Override
    public boolean isCollected(Long userId, Long collectUserId) {
        return lambdaQuery()
                .eq(UserFavorite::getUserId, userId)
                .eq(UserFavorite::getCollectUserId, collectUserId)
                .exists();
    }

    @Override
    public List<Long> listFavoriteUserIds(Long userId) {
        return lambdaQuery()
                .eq(UserFavorite::getUserId, userId)
                .list()
                .stream()
                .map(UserFavorite::getCollectUserId)
                .toList();
    }

    @Override
    public boolean deleteFavorite(Long userId, Long collectUserId) {
        return lambdaUpdate()
                .eq(UserFavorite::getUserId, userId)
                .eq(UserFavorite::getCollectUserId, collectUserId)
                .remove();
    }
}