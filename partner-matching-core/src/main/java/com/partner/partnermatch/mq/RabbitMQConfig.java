package com.partner.partnermatch.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "rabbitmq.enabled", havingValue = "true")
public class RabbitMQConfig {

    public static final String QUEUE = "recommend.cache.queue";
    public static final String EXCHANGE = "recommend.cache.exchange";
    public static final String ROUTING_KEY = "recommend.cache.refresh";

    @Bean
    public Queue recommendCacheQueue() {
        return new Queue(QUEUE, true, false, false);
    }

    @Bean
    public DirectExchange recommendCacheExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    @Bean
    public Binding recommendCacheBinding() {
        return BindingBuilder.bind(recommendCacheQueue()).to(recommendCacheExchange()).with(ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
