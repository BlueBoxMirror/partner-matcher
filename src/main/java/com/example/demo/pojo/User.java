package com.example.demo.pojo;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class User {
    private Long id;
    private String qqEmail;
    private String username;
    private byte[] password;
    private Integer gender;
    private String avatarUri;
    private String profile;
    private Integer collectNumber;
    private String tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer isDeleted;
}