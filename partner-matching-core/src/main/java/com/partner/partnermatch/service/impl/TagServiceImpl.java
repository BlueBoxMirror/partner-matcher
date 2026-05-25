package com.partner.partnermatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.partner.partnermatch.entity.UserTag;
import com.partner.partnermatch.event.TagChangedEvent;
import com.partner.partnermatch.mapper.UserTagMapper;
import com.partner.partnermatch.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
//todo 这个类无完整业务 后续需要替换
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private UserTagMapper userTagMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void updateTags(Long userId, List<Integer> tagIds) {
        userTagMapper.delete(new QueryWrapper<UserTag>().eq("user_id", userId));

        for (Integer tagId : tagIds) {
            UserTag ut = new UserTag();
            ut.setUserId(userId);
            ut.setTagId(tagId);
            userTagMapper.insert(ut);
        }
        //todo 做标签的同学： 在上边更新数据库完成后保留下边发消息行为 触发缓存更新
        eventPublisher.publishEvent(new TagChangedEvent(this, userId));
    }
}
