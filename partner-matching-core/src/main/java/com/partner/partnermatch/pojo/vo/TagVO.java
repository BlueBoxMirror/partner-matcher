package com.partner.partnermatch.pojo.vo;

import com.partner.partnermatch.entity.ai.AITag;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagVO {
    public TagVO(AITag tag){
        this(tag.getId(), tag.getTagName(), tag.getTagType());
    }
    private Integer tagId;
    private String tagName;
    private String tagType;
}
