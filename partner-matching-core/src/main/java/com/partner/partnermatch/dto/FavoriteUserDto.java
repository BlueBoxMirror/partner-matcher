package com.partner.partnermatch.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class FavoriteUserDto {
    private Integer id; // 收藏记录ID
    private Integer collectUserId; // 被收藏用户ID
    private String username; // 用户名
    private String avatar; // 用户头像
    private Date createdAt; // 收藏时间
    private List<TagDto> tags; // 用户标签

    @Data
    public static class TagDto {
        private Integer id;
        private String tag;
    }
}
