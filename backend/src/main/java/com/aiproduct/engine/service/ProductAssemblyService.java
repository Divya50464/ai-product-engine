package com.aiproduct.engine.service;

import com.aiproduct.engine.dto.FieldResultDTO;
import com.aiproduct.engine.dto.ProductSchemaDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductAssemblyService {

    private final ExtractionService extractionService;
    private final ConflictResolverService conflictResolverService;

    public ProductAssemblyService(
            ExtractionService extractionService,
            ConflictResolverService conflictResolverService) {

        this.extractionService = extractionService;
        this.conflictResolverService = conflictResolverService;
    }

    /**
     * Extracts product information from multiple sources
     * and resolves conflicts field by field.
     */
    public ProductSchemaDTO assemble(
            List<SourceInput> sources) {

        if (sources == null || sources.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one source is required"
            );
        }

        List<ExtractedSource> extractedSources = new ArrayList<>();

        for (SourceInput source : sources) {

            ProductSchemaDTO product =
                    extractionService.extractProduct(
                            source.text()
                    );

            extractedSources.add(
                    new ExtractedSource(
                            source.source(),
                            source.authorityTier(),
                            product
                    )
            );
        }

        return resolveProduct(extractedSources);
    }

    private ProductSchemaDTO resolveProduct(
            List<ExtractedSource> sources) {

        ProductSchemaDTO result = new ProductSchemaDTO();

        result.setName(
                resolveStringField(
                        "name",
                        sources
                )
        );

        result.setCategory(
                resolveStringField(
                        "category",
                        sources
                )
        );

        result.setPrice(
                resolveDoubleField(
                        "price",
                        sources
                )
        );

        result.setMaterial(
                resolveStringField(
                        "material",
                        sources
                )
        );

        result.setDimensions(
                resolveStringField(
                        "dimensions",
                        sources
                )
        );

        result.setWeight(
                resolveDoubleField(
                        "weight",
                        sources
                )
        );

        return result;
    }

    private String resolveStringField(
            String fieldName,
            List<ExtractedSource> sources) {

        List<FieldResultDTO> candidates = new ArrayList<>();

        for (ExtractedSource source : sources) {

            String value = getStringValue(
                    fieldName,
                    source.product()
            );

            if (value != null && !value.isBlank()) {

                candidates.add(
                        createCandidate(
                                fieldName,
                                value,
                                source
                        )
                );
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }

        return conflictResolverService
                .resolve(fieldName, candidates)
                .getValue();
    }

    private Double resolveDoubleField(
            String fieldName,
            List<ExtractedSource> sources) {

        List<FieldResultDTO> candidates = new ArrayList<>();

        for (ExtractedSource source : sources) {

            Double value = getDoubleValue(
                    fieldName,
                    source.product()
            );

            if (value != null) {

                candidates.add(
                        createCandidate(
                                fieldName,
                                String.valueOf(value),
                                source
                        )
                );
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }

        String resolvedValue =
                conflictResolverService
                        .resolve(fieldName, candidates)
                        .getValue();

        return Double.valueOf(resolvedValue);
    }

    private FieldResultDTO createCandidate(
            String fieldName,
            String value,
            ExtractedSource source) {

        FieldResultDTO candidate =
                new FieldResultDTO();

        candidate.setFieldName(fieldName);
        candidate.setValue(value);
        candidate.setSource(source.source());
        candidate.setAuthorityTier(
                source.authorityTier()
        );

        /*
         * Initial MVP confidence.
         *
         * Later this can come directly from
         * Gemini's field-level confidence.
         */
        candidate.setConfidence(
                authorityBasedConfidence(
                        source.authorityTier()
                )
        );

        return candidate;
    }

    private double authorityBasedConfidence(
            int authorityTier) {

        return switch (authorityTier) {
            case 1 -> 0.92;
            case 2 -> 0.75;
            case 3 -> 0.55;
            case 4 -> 0.30;
            default -> 0.20;
        };
    }

    private String getStringValue(
            String fieldName,
            ProductSchemaDTO product) {

        return switch (fieldName) {
            case "name" -> product.getName();
            case "category" -> product.getCategory();
            case "material" -> product.getMaterial();
            case "dimensions" -> product.getDimensions();
            default -> null;
        };
    }

    private Double getDoubleValue(
            String fieldName,
            ProductSchemaDTO product) {

        return switch (fieldName) {
            case "price" -> product.getPrice();
            case "weight" -> product.getWeight();
            default -> null;
        };
    }

    public record SourceInput(
            String source,
            int authorityTier,
            String text) {
    }

    private record ExtractedSource(
            String source,
            int authorityTier,
            ProductSchemaDTO product) {
    }
}