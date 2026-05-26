package com.partner.partnermatch.dto;

import lombok.Data;
import java.util.List;

@Data
public class FavoriteUserDto {
    private Long id;
    private String username;
    private String avatar;
    private List<TagDto> tags;

    @Data
    public static class TagDto {
        private Integer id;
        private String tag;
    }
}
