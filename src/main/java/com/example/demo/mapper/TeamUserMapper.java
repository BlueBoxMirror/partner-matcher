package com.example.demo.mapper;
import com.example.demo.pojo.TeamUser;
import org.apache.ibatis.annotations.*;
import java.util.List;
@Mapper
public interface TeamUserMapper {
   @Insert("INSERT INTO team_users (team_id, user_id, is_leader, join_time, create_time, update_time, is_deleted) " +
            "VALUES (#{teamId}, #{userId}, #{isLeader}, NOW(), NOW(), NOW(), 0)")
    int insert(TeamUser teamUser);
    @Select("SELECT COUNT(*) FROM team_users WHERE team_id = #{teamId} AND is_deleted = 0")
    int countMembersByTeamId(Long teamId);
    @Select("SELECT team_id FROM team_users WHERE user_id = #{userId} AND is_deleted = 0")
    List<Long> selectTeamIdsByUserId(Long userId);
    @Select("SELECT COUNT(*) FROM team_users WHERE team_id = #{teamId} AND user_id = #{userId} AND is_deleted = 0")
    int existsUserInTeam(@Param("teamId") Long teamId, @Param("userId") Long userId);
    @Select("SELECT is_leader FROM team_users WHERE team_id = #{teamId} AND user_id = #{userId} AND is_deleted = 0")
    Integer selectLeaderStatus(@Param("teamId") Long teamId, @Param("userId") Long userId);
    @Select("SELECT user_id FROM team_users WHERE team_id = #{teamId} AND is_deleted = 0")
    List<Long> selectUserIdsByTeamId(Long teamId);
}