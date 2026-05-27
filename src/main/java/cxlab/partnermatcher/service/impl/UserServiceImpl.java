package cxlab.partnermatcher.service.impl;

import cxlab.partnermatcher.mapper.UserMapper;
import cxlab.partnermatcher.pojo.UpdateUserRequest;
import cxlab.partnermatcher.pojo.UserV0;
import cxlab.partnermatcher.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserV0 getUserProfile(Long userId) {
        UserV0 user = userMapper.selectUserById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        List<String> tags = userMapper.selectTagsByUserId(userId);
        user.setTags(tags);
        return user;
    }

    @Override
    @Transactional
    public void updateUserProfile(Long userId, UpdateUserRequest request) {
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            userMapper.updateUsername(userId, request.getUsername());
        }
        if (request.getProfile() != null) {
            userMapper.updateProfile(userId, request.getProfile());
        }
        if (request.getTags() != null) {
            if (request.getTags().size() > 5) {
                throw new RuntimeException("最多选择5个标签");
            }

            userMapper.deleteUserTags(userId);

            for (String tagName : request.getTags()) {
                Long tagId = userMapper.getTagIdByTagName(tagName);
                if (tagId == null) {
                    throw new RuntimeException("标签不存在: " + tagName);
                }
                userMapper.insertUserTag(userId, tagId);
            }
        }
    }

    @Override
    public List<String> getAllTags() {
        return userMapper.selectAllTagNames();
    }
}
