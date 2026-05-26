package com.partner.partnermatch.entity.ai;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("user_tag")
@NoArgsConstructor
@AllArgsConstructor
public class AIUserTag {

    private Long userId;

    private Integer tagId;
}
