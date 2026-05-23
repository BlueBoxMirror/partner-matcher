package com.partner.partnermatch.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("tags")
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String tag;
}
