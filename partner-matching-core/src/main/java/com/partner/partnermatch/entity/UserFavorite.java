package com.partner.partnermatch.entity;


import lombok.Data;
import java.util.Date;

@Data
public class UserFavorite {
    private Integer id;
    private Long userId;
    private Long collectUserId;
    private Date createdAt;
}
