package com.partner.partnermatch.listener;

import com.partner.partnermatch.event.TagChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TagChangedListener {

    private static final Logger log = LoggerFactory.getLogger(TagChangedListener.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;

    @EventListener
    public void onTagChanged(TagChangedEvent event) {
        try {
            doOnTagChanged(event);
        } catch (Exception e) {
            // 缓存操作仅预热优化，所有异常在此消化，不影响用户
            log.warn("缓存刷新失败 userId={}: {}", event.getUserId(), e.getMessage());
        }
    }

    private void doOnTagChanged(TagChangedEvent event) {
        Long userId = event.getUserId();
        String dedupKey = "recommend:updating:" + userId;

        Boolean acquired = stringRedisTemplate.opsForValue().setIfAbsent(dedupKey, "1", 30, TimeUnit.SECONDS);
        if (!Boolean.TRUE.equals(acquired)) {
            log.info("去重拦截：{}s 内已有刷新任务，跳过 userId={}", dedupKey, userId);
            return;
        }

        // 先删缓存，保证即使 MQ 失败，下次请求也能内联重建
        String cacheKey = "recommend:" + userId;
        stringRedisTemplate.delete(cacheKey);
        log.info("已删除旧缓存 userId={}", userId);

        if (rabbitTemplate != null) {
            rabbitTemplate.convertAndSend("recommend.cache.exchange", "recommend.cache.refresh", userId);
            log.info("已发送 MQ 刷新 userId={}", userId);
        }
    }
}
