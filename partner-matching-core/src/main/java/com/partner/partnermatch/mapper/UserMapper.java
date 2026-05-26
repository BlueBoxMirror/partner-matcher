package com.partner.partnermatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.partner.partnermatch.entity.ai.AIUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<AIUser> {

}
