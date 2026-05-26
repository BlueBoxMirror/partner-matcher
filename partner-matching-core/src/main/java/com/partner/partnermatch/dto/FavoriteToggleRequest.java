package com.partner.partnermatch.dto;

import lombok.Data;

@Data
public class FavoriteToggleRequest {
    // 被收藏的用户ID
    private Long collectUserId;
}
