package com.example.demo.DTO;
import lombok.Data;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
@Data
public class TeamListRequest {
    private String keyword;
    @NotNull @Min(1) private Integer pageNum;
    @NotNull @Min(1) private Integer pageSize;
}