package com.example.demo.mapper;
import com.example.demo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE username = #{username} AND is_deleted = 0")
    User selectByUsername(String username);
}