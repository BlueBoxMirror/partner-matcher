package com.partner.partnermatch.mapper;

import com.partner.partnermatch.dto.FavoriteUserDto;
import com.partner.partnermatch.entity.UserFavorite;
import org.apache.ibatis.annotations.*;


import java.util.List;


@Mapper
public interface UserFavoriteMapper {

    @Select("SELECT COUNT(*) FROM user_favorite WHERE user_id = #{userId} AND collect_user_id = #{collectUserId}")
    int selectCount(@Param("userId") Long userId, @Param("collectUserId") Long collectUserId);

    @Insert("INSERT INTO user_favorite(user_id, collect_user_id, create_time) VALUES(#{userId}, #{collectUserId}, NOW())")
    void insertFavorite(UserFavorite favorite);

    @Delete("DELETE FROM user_favorite WHERE user_id = #{userId} AND collect_user_id = #{collectUserId}")
    void deleteFavorite(@Param("userId") Long userId, @Param("collectUserId") Long collectUserId);

    // 关键修改：SQL 别名和你 DTO 字段完全对应
    @Select("SELECT " +
            "uf.collect_user_id AS collectUserId, " +
            "u.id AS id, " +
            "u.username AS username, " +
            "u.avatar_url AS avatar, " +
            "uf.create_time AS createdAt " +
            "FROM user_collectins uf " +
            "LEFT JOIN users u ON uf.collect_user_id = u.id " +
            "WHERE uf.user_id = #{userId}")
    List<FavoriteUserDto> selectFavoriteList(@Param("userId") Long userId);
}