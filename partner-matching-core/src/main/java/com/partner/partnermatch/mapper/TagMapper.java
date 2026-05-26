package com.partner.partnermatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.partner.partnermatch.entity.ai.AITag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper extends BaseMapper<AITag> {
}
