package com.example.demo.DTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class TeamListResponse {
    private Long total;
    private List<TeamItem> list;

    @Data
    public static class TeamItem {
        private Long teamId;
        private String name;
        private String description;
        private Integer maxNum;
        private Integer currentNum;
        private LocalDateTime expireTime;
        private Boolean isEncrypted;
        private Boolean isFull;
        private LocalDateTime createTime;
        private List<Long> members;
        private Long leaderId;
    }
}