package com.partner.partnermatch.entity.ai;

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
public class AITag {
    public AITag(Integer id, String tagName) {
        this.id = id;
        this.tagName = tagName;
    }

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String tagName;
    private String tagType;
}
