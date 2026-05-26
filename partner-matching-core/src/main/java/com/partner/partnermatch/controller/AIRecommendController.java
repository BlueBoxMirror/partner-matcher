package com.partner.partnermatch.controller;

import com.partner.partnermatch.common.Result;
import com.partner.partnermatch.dto.AIUserDto;
import com.partner.partnermatch.service.AIRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ai/recommend")
public class AIRecommendController {
    @Autowired
    private AIRecommendService aiRecommendService;

    @GetMapping("/{id}")
    public Result<List<AIUserDto>> recommend(@PathVariable("id") Long id) {
        return aiRecommendService.recommend(id);
    }

}
