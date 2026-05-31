package com.example.demo.controller;
import com.example.demo.pojo.Result;
import com.example.demo.pojo.UserContext;
import com.example.demo.DTO.TeamCreateRequest;
import com.example.demo.DTO.TeamListRequest;
import com.example.demo.DTO.TeamListResponse;
import com.example.demo.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/v1/team")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    @PostMapping("/create")
    public Result<Long> createTeam(@Validated @RequestBody TeamCreateRequest request) {
        Long userId = UserContext.getCurrentUserId();
        log.info("创建队伍请求, userId={}, request={}", userId, request);
        Long teamId = teamService.createTeam(request, userId);
        log.info("创建队伍成功, teamId={}", teamId);
        return Result.success(teamId);
    }
    @GetMapping("/list/public")
    public Result<TeamListResponse> listPublicTeams(@Validated TeamListRequest request) {
        log.info("查询公开队伍列表, request={}", request);
        TeamListResponse response = teamService.listPublicTeams(request);
        return Result.success(response);
    }
    @GetMapping("/list/my/create")
    public Result<TeamListResponse> listMyCreatedTeams(@Validated TeamListRequest request) {
        Long userId = UserContext.getCurrentUserId();
        log.info("查询我创建的队伍, userId={}, request={}", userId, request);
        TeamListResponse response = teamService.listMyCreatedTeams(request, userId);
        return Result.success(response);
    }
    @GetMapping("/list/my/join")
    public Result<TeamListResponse> listMyJoinedTeams(@Validated TeamListRequest request) {
        Long userId = UserContext.getCurrentUserId();
        log.info("查询我加入的队伍, userId={}, request={}", userId, request);
        TeamListResponse response = teamService.listMyJoinedTeams(request, userId);
        return Result.success(response);
    }
}