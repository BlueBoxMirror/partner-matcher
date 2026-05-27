package com.partner.partnermatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.partner.partnermatch.entity.UserFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface UserFavoriteMapper extends BaseMapper<UserFavorite> {
    // 查询用户收藏的ID列表（兼容旧逻辑）
    @Select("SELECT collect_user_id FROM user_collections WHERE user_id = #{userId} ORDER BY created_at DESC")
    List<Long> listCollectUserIds(@Param("userId") Long userId);
}
