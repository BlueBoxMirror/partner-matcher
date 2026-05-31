package com.example.demo.mapper;
import com.example.demo.pojo.Team;
import org.apache.ibatis.annotations.*;
import java.util.List;
@Mapper
public interface TeamMapper {
    @Insert("INSERT INTO teams (members, member_num, type, team_name, description, max_num, expire_time, password, create_user_id, created_at, updated_at, is_deleted) " +
            "VALUES ('[]', '0', '', #{teamName}, #{description}, #{maxNum}, #{expireTime}, #{password}, #{createUserId}, NOW(), NOW(), 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Team team);
    @Select("SELECT * FROM teams WHERE team_name = #{teamName} AND is_deleted = 0")
    Team selectByName(String teamName);
  @Select("SELECT * FROM teams WHERE id = #{id} AND is_deleted = 0")
    Team selectById(Long id);
  List<Team> selectPublicTeams(@Param("keyword") String keyword);
    @Select("SELECT COUNT(*) FROM teams WHERE is_deleted = 0 AND expire_time > NOW() " +
            "AND (password IS NULL OR password = '') " +
            "AND (team_name LIKE CONCAT('%', #{keyword}, '%') OR #{keyword} IS NULL)")
    long countPublicTeams(@Param("keyword") String keyword);
   List<Team> selectByCreator(@Param("creatorId") Long creatorId);
   @Select("SELECT COUNT(*) FROM teams WHERE create_user_id = #{creatorId} AND is_deleted = 0")
    long countByCreator(Long creatorId);
    List<Team> selectByIdsWithPage(@Param("ids") List<Long> ids);
}