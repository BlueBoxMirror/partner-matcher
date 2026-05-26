package com.partner.partnermatch.dto;

import lombok.Data;
import java.util.List;
//测试用的 具体开发时可以去掉
@Data
public class TagUpdateRequest {
    private List<Integer> tagIds;
}
