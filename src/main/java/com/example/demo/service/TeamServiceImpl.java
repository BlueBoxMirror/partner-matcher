package com.example.demo.service;
import com.example.demo.DTO.TeamCreateRequest;
import com.example.demo.DTO.TeamListRequest;
import com.example.demo.DTO.TeamListResponse;
import com.example.demo.pojo.Team;
import com.example.demo.pojo.TeamUser;
import com.example.demo.mapper.TeamMapper;
import com.example.demo.mapper.TeamUserMapper;
import com.example.demo.service.TeamService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamMapper teamMapper;
    private final TeamUserMapper teamUserMapper;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    @Transactional
    public Long createTeam(TeamCreateRequest request, Long userId) {
        if (teamMapper.selectByName(request.getName()) != null) {
            throw new RuntimeException("队伍名称已存在");
        }
        if (request.getMaxNum() < 2 || request.getMaxNum() > 10) {
            throw new RuntimeException("最大人数范围2-10");
        }
        LocalDateTime expireTime;
        try {
            expireTime = LocalDateTime.parse(request.getExpireTime(), DATE_FORMATTER);
        } catch (Exception e) {
            throw new RuntimeException("过期时间格式错误");
        }
        if (expireTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("过期时间不能早于当前时间");
        }
        boolean hasPassword = request.getPassword() != null && !request.getPassword().isEmpty();
        if (hasPassword && (request.getPassword().length() < 4 || request.getPassword().length() > 20)) {
            throw new RuntimeException("加密队伍密码长度4-20位");
        }
        Team team = new Team();
        team.setTeamName(request.getName());
        team.setDescription(request.getDescription());
        team.setMaxNum(request.getMaxNum());
        team.setExpireTime(expireTime);
        team.setPassword(hasPassword ? request.getPassword() : null);
        team.setCreateUserId(userId);
        team.setMembers("[]");
        team.setMemberNum("0");
        team.setType("");
        team.setIsDeleted(0);
        teamMapper.insert(team);
        Long teamId = team.getId();
        TeamUser teamUser = new TeamUser();
        teamUser.setTeamId(teamId);
        teamUser.setUserId(userId);
        teamUser.setIsLeader(1);
        teamUser.setIsDeleted(0);
        teamUserMapper.insert(teamUser);
        log.info("队伍创建成功, teamId={}, userId={}", teamId, userId);
        return teamId;
    }
    @Override
    public TeamListResponse listPublicTeams(TeamListRequest request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<Team> teams = teamMapper.selectPublicTeams(request.getKeyword());
        PageInfo<Team> pageInfo = new PageInfo<>(teams);
        List<TeamListResponse.TeamItem> items = buildTeamItems(teams);
        TeamListResponse response = new TeamListResponse();
        response.setTotal(pageInfo.getTotal());
        response.setList(items);
        log.info("查询公开队伍列表完成, total={}", pageInfo.getTotal());
        return response;
    }
    @Override
    public TeamListResponse listMyCreatedTeams(TeamListRequest request, Long userId) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<Team> teams = teamMapper.selectByCreator(userId);
        PageInfo<Team> pageInfo = new PageInfo<>(teams);
        List<TeamListResponse.TeamItem> items = buildTeamItems(teams);
        TeamListResponse response = new TeamListResponse();
        response.setTotal(pageInfo.getTotal());
        response.setList(items);
        log.info("查询我创建的队伍完成, userId={}, total={}", userId, pageInfo.getTotal());
        return response;
    }
    @Override
    public TeamListResponse listMyJoinedTeams(TeamListRequest request, Long userId) {
        List<Long> teamIds = teamUserMapper.selectTeamIdsByUserId(userId);
        if (teamIds.isEmpty()) {
            TeamListResponse res = new TeamListResponse();
            res.setTotal(0L);
            res.setList(new ArrayList<>());
            log.info("查询我加入的队伍为空, userId={}", userId);
            return res;
        }
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<Team> teams = teamMapper.selectByIdsWithPage(teamIds);
        PageInfo<Team> pageInfo = new PageInfo<>(teams);
        List<TeamListResponse.TeamItem> items = buildTeamItems(teams);
        TeamListResponse response = new TeamListResponse();
        response.setTotal(pageInfo.getTotal());
        response.setList(items);
        log.info("查询我加入的队伍完成, userId={}, total={}", userId, pageInfo.getTotal());
        return response;
    }
    private List<TeamListResponse.TeamItem> buildTeamItems(List<Team> teams) {
        return teams.stream().map(team -> {
            TeamListResponse.TeamItem item = new TeamListResponse.TeamItem();
            item.setTeamId(team.getId());
            item.setName(team.getTeamName());
            item.setDescription(team.getDescription());
            item.setMaxNum(team.getMaxNum());
            int currentNum = teamUserMapper.countMembersByTeamId(team.getId());
            item.setCurrentNum(currentNum);
            item.setExpireTime(team.getExpireTime());
            item.setIsEncrypted(team.getPassword() != null && !team.getPassword().isEmpty());
            item.setIsFull(currentNum >= team.getMaxNum());
            item.setCreateTime(team.getCreatedAt());
            List<Long> memberIds = teamUserMapper.selectUserIdsByTeamId(team.getId());
            item.setMembers(memberIds);
            item.setLeaderId(team.getCreateUserId());
            return item;
        }).collect(Collectors.toList());
    }
}