package com.partner.partnermatch.dto;

import com.partner.partnermatch.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AIUserDto {
    private Long id;

    private String username;

    private String gender;

    private String background;

    private List<Tag> tags;
}
