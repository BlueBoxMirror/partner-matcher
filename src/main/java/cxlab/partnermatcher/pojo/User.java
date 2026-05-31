package cxlab.partnermatcher.pojo;

import com.fasterxml.jackson.annotation.JsonProperty; // 必须导入，不然注解不生效
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String qqEmail;
    private String username;
    private byte[] password; // 数据库字段：存加密后的SHA-256字节数组，完全不变
    @JsonProperty("password") // 关键：前端传的"password"会自动映射到这个字段，不用改前端
    private String pwd; // 接收前端明文密码，改名避免和数据库字段冲突
    private String code;
    private Integer gender;
    private String avatarUri;
    private String profile;
    private Integer collectNumber = 0;
    private String tags = "[]";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}