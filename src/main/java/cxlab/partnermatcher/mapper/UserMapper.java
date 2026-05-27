package cxlab.partnermatcher.mapper;

import cxlab.partnermatcher.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    // 把方法名改成和 Service 里调用的一致，参数名也和 XML 里的 #{keyword} 对应
    User selectByUsernameOrEmail(@Param("keyword") String keyword);
}