package com.aiproduct.engine.service;

import com.aiproduct.engine.ai.GeminiClientWrapper;
import com.aiproduct.engine.dto.ProductSchemaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductAssemblyServiceIntegrationTest {

    @Test
    void shouldExtractAndResolveProductFromMultipleSources() {

        // Real Gemini client
        GeminiClientWrapper wrapper =
                TestGeminiFactory.createWrapper();

        // Services
        ExtractionService extractionService =
                new ExtractionService(
                        wrapper,
                        new ObjectMapper()
                );

        ConflictResolverService conflictResolverService =
                new ConflictResolverService();

        ProductAssemblyService assemblyService =
                new ProductAssemblyService(
                        extractionService,
                        conflictResolverService
                );

        // Two sources with conflicting prices.
        List<ProductAssemblyService.SourceInput> sources =
                List.of(

                        new ProductAssemblyService.SourceInput(
                                "Official Apple Store",
                                1,
                                """
                                Apple iPhone 16
                                Category: Smartphone
                                Price: ₹79,900
                                Material: Aluminum
                                Dimensions: 147.6 x 71.6 x 7.8 mm
                                Weight: 170 g
                                """
                        ),

                        new ProductAssemblyService.SourceInput(
                                "Third Party Retailer",
                                2,
                                """
                                Apple iPhone 16
                                Category: Smartphone
                                Price: ₹74,900
                                Material: Aluminum
                                Dimensions: 147.6 x 71.6 x 7.8 mm
                                Weight: 170 g
                                """
                        )
                );

        // Execute complete pipeline
        ProductSchemaDTO result =
                assemblyService.assemble(sources);

        // Verify final result
        assertNotNull(result);

        assertEquals(
                "Apple iPhone 16",
                result.getName()
        );

        // Tier 1 should win over Tier 2.
        assertEquals(
                79900.0,
                result.getPrice()
        );

        System.out.println(
                "Final assembled product: " + result
        );
    }
}