package cxlab.partnermatcher.mapper;

import cxlab.partnermatcher.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User selectByAccountOrEmail(@Param("account") String account);
}