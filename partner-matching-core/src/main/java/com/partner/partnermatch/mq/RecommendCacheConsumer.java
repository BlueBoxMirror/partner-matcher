package com.partner.partnermatch.mq;

import com.partner.partnermatch.service.AIRecommendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "rabbitmq.enabled", havingValue = "true")
public class RecommendCacheConsumer {

    private static final Logger log = LoggerFactory.getLogger(RecommendCacheConsumer.class);

    @Autowired
    private AIRecommendService aiRecommendService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void refreshCache(Long userId) {
        log.info("收到缓存刷新消息 userId={}", userId);
        aiRecommendService.refreshCandidateCache(userId);
        log.info("缓存刷新完成 userId={}", userId);
    }
}
