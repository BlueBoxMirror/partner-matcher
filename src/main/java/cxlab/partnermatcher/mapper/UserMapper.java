package cxlab.partnermatcher.mapper;

import cxlab.partnermatcher.pojo.UserV0;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT id, username, avatar_uri, profile FROM users WHERE id = #{id}")
    UserV0 selectUserById(@Param("id") Long id);
    @Select("SELECT t.tag_name FROM tags t " +
            "JOIN user_tag ut ON t.id = ut.tag_id " +
            "WHERE ut.user_id = #{userId}")
    List<String> selectTagsByUserId(@Param("userId") Long userId);

    @Update("UPDATE users SET username = #{username} WHERE id = #{id}")
    void updateUsername(@Param("id") Long id, @Param("username") String username);
    @Update("UPDATE users SET profile = #{profile} WHERE id = #{id}")
    void updateProfile(@Param("id") Long id, @Param("profile") String profile);
    @Update("UPDATE users SET avatar_uri = #{avatarUri} WHERE id = #{id}")
    void updateAvatarUri(@Param("id") Long id, @Param("avatarUri") String avatarUri);
    @Delete("DELETE FROM user_tag WHERE user_id = #{userId}")
    void deleteUserTags(@Param("userId") Long userId);
    @Select("SELECT id FROM tags WHERE tag_name = #{tagName}")
    Long getTagIdByTagName(@Param("tagName") String tagName);
    @Insert("INSERT INTO user_tag (user_id, tag_id) VALUES (#{userId}, #{tagId})")
    void insertUserTag(@Param("userId") Long userId, @Param("tagId") Long tagId);
    @Select("SELECT tag_name FROM tags ORDER BY tag_type, id")
    List<String> selectAllTagNames();
}