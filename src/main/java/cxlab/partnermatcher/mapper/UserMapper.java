package cxlab.partnermatcher.mapper;

import cxlab.partnermatcher.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    User selectByUsernameOrEmail(@Param("keyword") String keyword);
    int insert(User user);
}