package com.aiproduct.engine.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
public class GeminiClientWrapper {

    private final ChatClient chatClient;

    public GeminiClientWrapper(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String generate(String prompt) {
        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }
}