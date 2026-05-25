package com.partner.partnermatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIRecommendResponse {
    private Long id;
    private String reason;
}
