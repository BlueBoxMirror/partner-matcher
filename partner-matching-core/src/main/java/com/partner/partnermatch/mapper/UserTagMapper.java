package com.partner.partnermatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.partner.partnermatch.entity.UserTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserTagMapper extends BaseMapper<UserTag> {

    /**
     * 自连接查询：找到与当前用户共享标签的用户，按共享数量降序取 Top N
     * ut1 = 当前用户的标签记录，ut2 = 拥有相同标签的其他用户记录
     * GROUP BY + COUNT 统计每个匹配用户共享了几个标签
     */
    @Select("SELECT ut2.user_id " +
            "FROM user_tag ut1 " +
            "JOIN user_tag ut2 ON ut1.tag_id = ut2.tag_id " +
            "WHERE ut1.user_id = #{currentUserId} AND ut2.user_id != #{currentUserId} " +
            "GROUP BY ut2.user_id " +
            "ORDER BY COUNT(*) DESC " +
            "LIMIT #{limit}")
    List<Long> findMatchedUserIds(@Param("currentUserId") Long currentUserId, @Param("limit") int limit);

    /**
     * 一次 JOIN 查出指定用户的完整标签（含标签名），替代先查 user_tag 再查 tags 的两次往返
     */
    @Select("<script>" +
            "SELECT ut.user_id, t.id, t.tag " +
            "FROM user_tag ut JOIN tags t ON t.id = ut.tag_id " +
            "WHERE ut.user_id IN " +
            "<foreach collection='userIds' item='id' open='(' separator=',' close=')'>#{id}</foreach>" +
            "</script>")
    List<Map<String, Object>> findTagsByUserIds(@Param("userIds") List<Long> userIds);

    @Select("SELECT DISTINCT user_id FROM user_tag")
    List<Long> findAllUserIds();
}
