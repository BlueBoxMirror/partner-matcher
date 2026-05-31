package com.example.demo.pojo;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class Team {
    private Long id;
    private String members;
    private String memberNum;
    private String type;
    private String teamName;
    private String description;
    private Integer maxNum;
    private LocalDateTime expireTime;
    private String password;
    private Long createUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer isDeleted;
}