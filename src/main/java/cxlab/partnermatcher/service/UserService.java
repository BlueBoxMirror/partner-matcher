package cxlab.partnermatcher.service;

import cxlab.partnermatcher.pojo.UpdateUserRequest;
import cxlab.partnermatcher.pojo.UserV0;
import java.util.List;

public interface UserService {
    UserV0 getUserProfile(Long userId);
    void updateUserProfile(Long userId, UpdateUserRequest request);
    List<String> getAllTags();
}
