package com.aiproduct.engine.ai;

import com.google.genai.Client;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GeminiClientWrapperIntegrationTest {

    @Test
    void shouldCallGemini() {

        String apiKey = System.getenv("GOOGLE_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "GOOGLE_API_KEY environment variable is not set"
            );
        }

        Client genAiClient = Client.builder()
                .apiKey(apiKey)
                .build();

        GoogleGenAiChatModel chatModel =
        GoogleGenAiChatModel.builder()
                .genAiClient(genAiClient)
                .defaultOptions(
                        GoogleGenAiChatOptions.builder()
                                .model("gemini-3.6-flash")
                                .build()
                )
                .build();

        ChatClient chatClient =
                ChatClient.builder(chatModel)
                        .build();

        GeminiClientWrapper wrapper =
                new GeminiClientWrapper(chatClient);

        String response = wrapper.generate(
                "Reply with exactly: GEMINI_OK"
        );

        assertNotNull(response);
        assertFalse(response.isBlank());

        System.out.println(
                "Gemini response: " + response
        );
    }
}