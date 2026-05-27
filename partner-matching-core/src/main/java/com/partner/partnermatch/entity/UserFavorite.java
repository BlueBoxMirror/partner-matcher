package com.partner.partnermatch.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("user_collections")
public class UserFavorite {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("user_id")
    private Long userId;

    @TableField("collect_user_id")
    private Integer collectUserId;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Date createdAt;
}
