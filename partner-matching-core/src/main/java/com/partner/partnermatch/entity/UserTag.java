package com.partner.partnermatch.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("user_tag")
@NoArgsConstructor
@AllArgsConstructor
public class UserTag {

    private Long userId;


    private Integer tagId;
}
