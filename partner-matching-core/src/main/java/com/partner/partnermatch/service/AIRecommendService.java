package com.partner.partnermatch.service;

import com.partner.partnermatch.common.Result;
import com.partner.partnermatch.dto.AIUserDto;

import java.util.List;

public interface AIRecommendService {
    Result<List<AIUserDto>> recommend(Long id);

    /** 刷新候选池缓存：查库 Top 50 → shuffle → 写 Redis（TTL 3h），返回 candidate 列表 */
    List<AIUserDto> refreshCandidateCache(Long userId);
}
