package com.partner.partnermatch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.partner.partnermatch.entity.UserFavorite;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFavoriteService extends IService<UserFavorite> {
    // 收藏/取消收藏切换
    boolean toggleCollection(Long userId, Long collectUserId);
    // 判断是否已收藏
    boolean isCollected(@Param("userId") Long userId, @Param("collectUserId") Long collectUserId);
    // 获取收藏用户ID列表
    List<Long> listFavoriteUserIds(Long userId);
    // 删除指定收藏（单独取消收藏）
    boolean deleteFavorite(Long userId, Long collectUserId);
}
