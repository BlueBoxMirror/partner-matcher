package com.partner.partnermatch.dto;

import lombok.Data;
import java.util.List;

@Data
public class TagUpdateRequest {
    private List<Integer> tagIds;
}
