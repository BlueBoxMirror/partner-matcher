package com.partner.partnermatch.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.partner.partnermatch.dto.FavoritePageRequest;
import com.partner.partnermatch.dto.FavoriteUserDto;
import com.partner.partnermatch.entity.UserFavorite;
import java.util.List;

public interface UserFavoriteService extends IService<UserFavorite> {
    // 收藏/取消收藏切换
    boolean toggleCollection(Long userId, Integer collectUserId);

    // 判断是否已收藏
    boolean isCollected(Long userId, Integer collectUserId);

    // 获取收藏用户ID列表
    List<Long> listFavoriteUserIds(Long userId);

    // 删除指定收藏（单独取消收藏）
    boolean deleteFavorite(Long userId, Integer collectUserId);

    // 新增：分页查询收藏列表（按收藏时间倒序）
    Page<FavoriteUserDto> listFavoritesPage(Long userId, FavoritePageRequest request);
}