package com.partner.partnermatch.entity.ai;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("users")
public class AIUser {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private Integer gender;

    private String profile;

    private String avatarUri;

}
