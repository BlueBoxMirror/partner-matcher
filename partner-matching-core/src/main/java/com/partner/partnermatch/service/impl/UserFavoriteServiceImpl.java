package com.partner.partnermatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.partner.partnermatch.dto.FavoritePageRequest;
import com.partner.partnermatch.dto.FavoriteUserDto;
import com.partner.partnermatch.entity.UserFavorite;
import com.partner.partnermatch.mapper.UserFavoriteMapper;
import com.partner.partnermatch.service.UserFavoriteService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFavoriteServiceImpl extends ServiceImpl<UserFavoriteMapper, UserFavorite>
        implements UserFavoriteService {

    private final UserFavoriteMapper userFavoriteMapper;

    public UserFavoriteServiceImpl(UserFavoriteMapper userFavoriteMapper) {
        this.userFavoriteMapper = userFavoriteMapper;
    }

    @Override
    public boolean toggleCollection(Long userId, Long collectUserId) {
        // 不能收藏自己
        if (userId.equals(collectUserId)) {
            throw new RuntimeException("不能收藏自己");
        }
        boolean collected = isCollected(userId, collectUserId);
        if (collected) {
            // 已收藏：取消收藏
            return deleteFavorite(userId, collectUserId);
        } else {
            // 未收藏：新增收藏
            UserFavorite favorite = new UserFavorite();
            favorite.setUserId((long) Math.toIntExact(userId));
            favorite.setCollectUserId(collectUserId);
            return save(favorite);
        }
    }

    public boolean isCollected(Long userId, Long collectUserId) {
        return lambdaQuery()
                .eq(UserFavorite::getUserId, userId)
                .eq(UserFavorite::getCollectUserId, collectUserId)
                .count() > 0;
    }

    @Override
    public List<Long> listFavoriteUserIds(Long userId) {
        return userFavoriteMapper.listCollectUserIds(userId);
    }

    public boolean deleteFavorite(Long userId, Long collectUserId) {
        return lambdaUpdate()
                .eq(UserFavorite::getUserId, userId)
                .eq(UserFavorite::getCollectUserId, collectUserId)
                .remove();
    }

    @Override
    public Page<FavoriteUserDto> listFavoritesPage(Long userId, FavoritePageRequest request) {
        // 1. 构建分页对象
        Page<UserFavorite> page = new Page<>(request.getPageNum(), request.getPageSize());
        // 2. 按收藏时间倒序查询
        Page<UserFavorite> favoritePage = page(page, new LambdaQueryWrapper<UserFavorite>()
                .eq(UserFavorite::getUserId, userId)
                .orderByDesc(UserFavorite::getCreatedAt));
        // 3. 转换为DTO返回
        List<FavoriteUserDto> dtoList = favoritePage.getRecords().stream()
                .map(favorite -> {
                    FavoriteUserDto dto = new FavoriteUserDto();
                    dto.setId(Math.toIntExact(favorite.getId()));
                    dto.setCollectUserId((long) Math.toIntExact(favorite.getCollectUserId()));
                    dto.setCreatedAt(favorite.getCreatedAt());
                    // 后续补充用户信息时，在这里set username、avatar、tags
                    dto.setUsername("");
                    dto.setAvatar("");
                    dto.setTags(null);
                    return dto;
                }).collect(Collectors.toList());
        // 4. 构建分页结果
        Page<FavoriteUserDto> resultPage = new Page<>(request.getPageNum(), request.getPageSize(), favoritePage.getTotal());
        resultPage.setRecords(dtoList);
        return resultPage;
    }
}