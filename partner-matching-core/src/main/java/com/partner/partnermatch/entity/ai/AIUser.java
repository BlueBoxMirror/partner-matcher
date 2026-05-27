package com.partner.partnermatch.entity.ai;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "users",autoResultMap = true)
@Builder
public class AIUser {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String qqEmail;
    private String username;
    private byte[] password;

    private Integer gender;

    private String profile;

    private String avatarUri;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> tags;

}
