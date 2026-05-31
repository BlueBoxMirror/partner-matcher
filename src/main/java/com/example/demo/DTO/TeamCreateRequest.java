package com.example.demo.DTO;
import lombok.Data;
import jakarta.validation.constraints.*;
@Data
public class TeamCreateRequest {
    @NotBlank(message = "队伍名称不能为空")
    @Size(min = 1, max = 20, message = "队伍名称长度1-20位")
    private String name;
    @Size(max = 500, message = "队伍描述最大500字")
    private String description;
    @NotNull(message = "最大人数不能为空")
    @Min(2) @Max(100)
    private Integer maxNum;
    @NotBlank(message = "过期时间不能为空")
    private String expireTime;
    @Size(min = 4, max = 20, message = "密码长度4-20位")
    private String password;
}