package com.example.demo.pojo;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class TeamUser {
    private Long id;
    private Long teamId;
    private Long userId;
    private Integer isLeader;
    private LocalDateTime joinTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer isDeleted;
}