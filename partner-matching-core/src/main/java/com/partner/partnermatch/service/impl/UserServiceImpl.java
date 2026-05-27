package com.partner.partnermatch.service.impl;

import com.partner.partnermatch.entity.ai.AIUser;
import com.partner.partnermatch.mapper.UserMapper;
import com.partner.partnermatch.service.LuceneStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl {
    @Autowired private UserMapper userMapper;
    @Autowired private LuceneStorageService luceneStorageService;

    public void deleteUser(long userId) throws IOException {
        userMapper.deleteById(userId);
        luceneStorageService.deleteUser(userId);
    }

    public List<AIUser> getUsersByIds(Collection<Long> ids){
        return userMapper.selectByIds(ids);
    }
}
