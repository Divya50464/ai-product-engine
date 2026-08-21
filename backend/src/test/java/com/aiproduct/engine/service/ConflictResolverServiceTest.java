package com.aiproduct.engine.service;

import com.aiproduct.engine.dto.FieldResultDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConflictResolverServiceTest {

    private final ConflictResolverService resolver =
            new ConflictResolverService();

    @Test
    void shouldPreferHigherAuthoritySource() {

        FieldResultDTO officialSource = new FieldResultDTO(
                "price",
                "2000",
                0.92,
                1,
                "manufacturer-pdf",
                null,
                null,
                null
        );

        FieldResultDTO websiteSource = new FieldResultDTO(
                "price",
                "2500",
                0.95,
                3,
                "website",
                null,
                null,
                null
        );

        FieldResultDTO result = resolver.resolve(
                "price",
                List.of(websiteSource, officialSource)
        );

        assertEquals("2000", result.getValue());
        assertEquals("manufacturer-pdf", result.getSource());
        assertEquals("2500", result.getRejectedValue());
        assertEquals("website", result.getRejectedSource());
    }

    @Test
    void shouldPreferHigherConfidenceWhenAuthorityIsSame() {

        FieldResultDTO sourceOne = new FieldResultDTO(
                "weight",
                "2.0",
                0.80,
                2,
                "source-one",
                null,
                null,
                null
        );

        FieldResultDTO sourceTwo = new FieldResultDTO(
                "weight",
                "2.5",
                0.95,
                2,
                "source-two",
                null,
                null,
                null
        );

        FieldResultDTO result = resolver.resolve(
                "weight",
                List.of(sourceOne, sourceTwo)
        );

        assertEquals("2.5", result.getValue());
        assertEquals("source-two", result.getSource());
    }
}