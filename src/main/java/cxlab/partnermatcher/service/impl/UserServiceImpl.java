package cxlab.partnermatcher.service.impl;

import cxlab.partnermatcher.mapper.UserMapper;
import cxlab.partnermatcher.pojo.User;
import cxlab.partnermatcher.pojo.dto.UserLoginRequest;
import cxlab.partnermatcher.pojo.dto.UserLoginResponse;
import cxlab.partnermatcher.service.UserService;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserLoginResponse login(UserLoginRequest loginRequest) {
        // 1. 统一变量名，和 UserLoginRequest 里的 username 对应
        String username = loginRequest.getUsername();
        String pwd = loginRequest.getPassword();

        // 2. 参数校验（现在 username 和 password 能正常解析了）
        if (username == null || username.trim().isEmpty() || pwd == null || pwd.trim().isEmpty()) {
            throw new RuntimeException("账号或密码不能为空");
        }

        // 3. 修改为查询 username 或 qq_email，和数据库字段对应
        User user = userMapper.selectByUsernameOrEmail(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 4. 密码校验部分不用改，因为 SHA-256 生成的 32 字节和数据库 binary(32) 匹配
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] inputPwdBytes = sha256.digest(pwd.getBytes());
            if (!Arrays.equals(inputPwdBytes, user.getPassword())) {
                throw new RuntimeException("密码错误");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("密码校验异常");
        }

        String token = UUID.randomUUID().toString();
        return new UserLoginResponse(token, user.getId(), user.getUsername(), user.getQqEmail());
    }
}