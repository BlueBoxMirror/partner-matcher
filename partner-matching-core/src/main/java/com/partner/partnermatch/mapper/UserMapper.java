package com.partner.partnermatch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.partner.partnermatch.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
