package com.partner.partnermatch.mapper;

import com.partner.partnermatch.dto.FavoriteUserDto;
import com.partner.partnermatch.entity.UserFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Mapper
public interface UserFavoriteMapper {
    int selectCount(@Param("userId") Long userId, @Param("collectUserId") Long collectUserId);
    void insertFavorite(UserFavorite favorite);
    void deleteFavorite(@Param("userId") Long userId, @Param("collectUserId") Long collectUserId);
    List<FavoriteUserDto> selectFavoriteList(@Param("userId") Long userId);
}
