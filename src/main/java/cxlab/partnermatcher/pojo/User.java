package cxlab.partnermatcher.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String qqEmail;
    private String username;
    private byte[] password;
    @JsonProperty("password")
    private String pwd;
    private String code;
    private Integer gender;
    private String avatarUri;
    private String profile;
    private Integer collectNumber = 0;
    private String tags = "[]";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}