package com.partner.partnermatch.dto;

import com.partner.partnermatch.entity.ai.AITag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
// AI 推荐用户dto
public class AIUserDto {
    private Long id;

    private String username;

    private Integer gender;

    private String avatar;

    private String background;

    private List<AITag> tags;
}
