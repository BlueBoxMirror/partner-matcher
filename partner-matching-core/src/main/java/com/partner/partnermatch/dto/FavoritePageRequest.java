package com.partner.partnermatch.dto;

import lombok.Data;
import javax.validation.constraints.Min;

@Data
public class FavoritePageRequest {
    @Min(value = 1, message = "页码必须大于等于1")
    private int pageNum = 1;

    @Min(value = 1, message = "页大小必须大于等于1")
    private int pageSize = 10;
}
