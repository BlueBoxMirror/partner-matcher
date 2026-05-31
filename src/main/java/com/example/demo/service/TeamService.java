package com.example.demo.service;
import com.example.demo.DTO.TeamCreateRequest;
import com.example.demo.DTO.TeamListRequest;
import com.example.demo.DTO.TeamListResponse;
public interface TeamService {
    Long createTeam(TeamCreateRequest request, Long userId);
    TeamListResponse listPublicTeams(TeamListRequest request);
    TeamListResponse listMyCreatedTeams(TeamListRequest request, Long userId);
    TeamListResponse listMyJoinedTeams(TeamListRequest request, Long userId);
}