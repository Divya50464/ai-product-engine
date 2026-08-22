package com.aiproduct.engine.service;

import com.aiproduct.engine.ai.GeminiClientWrapper;
import com.aiproduct.engine.dto.ProductSchemaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExtractionServiceIntegrationTest {

    @Test
    void shouldExtractRealProductUsingGemini() {

        GeminiClientWrapper wrapper = TestGeminiFactory.createWrapper();

        ExtractionService service =
                new ExtractionService(wrapper, new ObjectMapper());

        String productText = """
                Apple iPhone 16
                Price: ₹79,900
                Storage: 128 GB
                Color: Black
                Weight: 170 g
                Display: 6.1 inch
                """;

        ProductSchemaDTO result = service.extractProduct(productText);

        assertNotNull(result);
        assertNotNull(result.getName());
        assertNotNull(result.getPrice());

        System.out.println(result);
    }
}
