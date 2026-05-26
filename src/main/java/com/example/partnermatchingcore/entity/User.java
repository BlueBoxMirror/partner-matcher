package com.example.partnermatchingcore.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String qqEmail;
    private String username;

    // 数据库 BINARY(32) 对应字段，只存加密后的二进制
    private byte[] password;

    // 专门用来接收前端传的明文密码，字段名是 pwd，避免和上面的 byte[] password 冲突
    private String pwd;

    private Integer gender;
    private String avatarUri;
    private String profile;
    private Integer collectNumber = 0;
    private String tags = "[]";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}