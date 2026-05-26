package com.example.partnermatchingcore.mapper;

import com.example.partnermatchingcore.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select * from users where username = #{username}")
    User findByUsername(String username);

    @Select("select * from users where qq_email = #{email}")
    User findByEmail(String email);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into users(qq_email, username, password, gender, avatar_uri, profile, collect_number, tags) " +
            "values(#{qqEmail}, #{username}, #{password}, #{gender}, #{avatarUri}, #{profile}, #{collectNumber}, #{tags})")
    int insert(User user);
}