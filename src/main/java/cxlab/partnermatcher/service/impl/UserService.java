package cxlab.partnermatcher.service.impl;

import cxlab.partnermatcher.pojo.User;
import cxlab.partnermatcher.pojo.dto.UserLoginRequest;
import cxlab.partnermatcher.pojo.dto.UserLoginResponse;

public interface UserService {
    UserLoginResponse login(UserLoginRequest loginRequest);
    User register(User user);
}