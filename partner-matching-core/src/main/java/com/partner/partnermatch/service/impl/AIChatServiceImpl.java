package com.partner.partnermatch.service.impl;

import com.partner.partnermatch.service.AIChatService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AIChatServiceImpl implements AIChatService {

    @Autowired(required = false)
    private ChatClient.Builder chatClientBuilder;

    private volatile ChatClient chatClient;

    private ChatClient getChatClient() {
        if (chatClient == null) {
            synchronized (this) {
                if (chatClient == null) {
                    if (chatClientBuilder == null) {
                        throw new IllegalStateException("AI模型未配置，请在application.yml中配置spring.ai.openai");
                    }
                    chatClient = chatClientBuilder.build();
                }
            }
        }
        return chatClient;
    }

    @Override
    public String chat(String prompt) {
        return getChatClient()
                .prompt()
                .user(prompt)
                .call()
                .content();
    }
}
