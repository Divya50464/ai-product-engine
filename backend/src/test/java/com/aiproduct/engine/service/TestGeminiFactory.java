package com.aiproduct.engine.service;

import com.aiproduct.engine.ai.GeminiClientWrapper;
import com.google.genai.Client;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;

public class TestGeminiFactory {

    public static GeminiClientWrapper createWrapper() {

        String apiKey = System.getenv("GOOGLE_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "GOOGLE_API_KEY environment variable is not set"
            );
        }

        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        GoogleGenAiChatModel model =
                GoogleGenAiChatModel.builder()
                        .genAiClient(client)
                        .defaultOptions(
                                GoogleGenAiChatOptions.builder()
                                        .model("gemini-3.6-flash")
                                        .build()
                        )
                        .build();

        ChatClient chatClient =
                ChatClient.builder(model)
                        .build();

        return new GeminiClientWrapper(chatClient);
    }
}