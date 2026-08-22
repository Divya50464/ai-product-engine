package com.aiproduct.engine.service;

import com.aiproduct.engine.ai.GeminiClientWrapper;
import com.aiproduct.engine.dto.ProductSchemaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ExtractionServiceTest {

    @Test
    void shouldConvertGeminiJsonIntoProductSchema() {

        GeminiClientWrapper geminiClient =
                mock(GeminiClientWrapper.class);

        ObjectMapper objectMapper =
                new ObjectMapper();

        String geminiResponse = """
                {
                  "name": "Premium Cotton T-Shirt",
                  "category": "Men's Clothing",
                  "price": 1499.0,
                  "material": "100% cotton",
                  "dimensions": "72 cm x 52 cm",
                  "weight": 250.0
                }
                """;

        when(geminiClient.generate(anyString()))
                .thenReturn(geminiResponse);

        ExtractionService extractionService =
                new ExtractionService(
                        geminiClient,
                        objectMapper
                );

        ProductSchemaDTO result =
                extractionService.extractProduct(
                        "Premium Cotton T-Shirt. Price ₹1499. Material 100% cotton."
                );

        assertEquals(
                "Premium Cotton T-Shirt",
                result.getName()
        );

        assertEquals(
                "Men's Clothing",
                result.getCategory()
        );

        assertEquals(
                1499.0,
                result.getPrice()
        );

        assertEquals(
                "100% cotton",
                result.getMaterial()
        );

        verify(geminiClient, times(1))
                .generate(anyString());
    }
}