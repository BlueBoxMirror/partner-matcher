package com.partner.partnermatch.dto;

import lombok.Data;

@Data
public class FavoriteToggleRequest {
    private Long userId;        // 收藏人ID
    private Long collectUserId;  // 被收藏人ID
}
