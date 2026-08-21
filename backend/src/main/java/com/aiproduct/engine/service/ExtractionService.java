package com.aiproduct.engine.service;

import com.aiproduct.engine.ai.GeminiClientWrapper;
import com.aiproduct.engine.ai.PromptTemplates;
import com.aiproduct.engine.dto.ProductSchemaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ExtractionService {

    private final GeminiClientWrapper geminiClient;
    private final ObjectMapper objectMapper;

    public ExtractionService(
            GeminiClientWrapper geminiClient,
            ObjectMapper objectMapper) {

        this.geminiClient = geminiClient;
        this.objectMapper = objectMapper;
    }

    public ProductSchemaDTO extractProduct(String sourceText) {

        if (sourceText == null || sourceText.isBlank()) {
            throw new IllegalArgumentException("Source text cannot be empty");
        }

        String prompt = PromptTemplates.PRODUCT_EXTRACTION
                + "\n\nSOURCE TEXT:\n"
                + sourceText;

        String response = geminiClient.generate(prompt);

        try {
            String cleanJson = cleanJsonResponse(response);

            return objectMapper.readValue(
                    cleanJson,
                    ProductSchemaDTO.class
            );

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to parse Gemini product extraction response",
                    e
            );
        }
    }

    private String cleanJsonResponse(String response) {

        if (response == null || response.isBlank()) {
            throw new IllegalStateException(
                    "Gemini returned an empty response"
            );
        }

        String cleaned = response.trim();

        // Remove Markdown JSON code fences if Gemini returns them.
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }

        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(
                    0,
                    cleaned.length() - 3
            );
        }

        return cleaned.trim();
    }
}