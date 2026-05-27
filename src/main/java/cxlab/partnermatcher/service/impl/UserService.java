package cxlab.partnermatcher.service;

import cxlab.partnermatcher.pojo.dto.UserLoginRequest;
import cxlab.partnermatcher.pojo.dto.UserLoginResponse;

public interface UserService {
    UserLoginResponse login(UserLoginRequest loginRequest);
}