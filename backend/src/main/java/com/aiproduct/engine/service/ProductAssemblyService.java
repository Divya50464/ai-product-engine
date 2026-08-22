package com.aiproduct.engine.service;

import com.aiproduct.engine.dto.FieldResultDTO;
import com.aiproduct.engine.dto.ProductSchemaDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        /*
         * Stores the complete resolution result for each field.
         *
         * Example:
         * price -> winning value, confidence, source,
         *          authority tier, rejected value, rejected source
         */
        Map<String, FieldResultDTO> fieldResults =
                new HashMap<>();

        result.setName(
                resolveStringField(
                        "name",
                        sources,
                        fieldResults
                )
        );

        result.setCategory(
                resolveStringField(
                        "category",
                        sources,
                        fieldResults
                )
        );

        result.setPrice(
                resolveDoubleField(
                        "price",
                        sources,
                        fieldResults
                )
        );

        result.setMaterial(
                resolveStringField(
                        "material",
                        sources,
                        fieldResults
                )
        );

        result.setDimensions(
                resolveStringField(
                        "dimensions",
                        sources,
                        fieldResults
                )
        );

        result.setWeight(
                resolveDoubleField(
                        "weight",
                        sources,
                        fieldResults
                )
        );

        result.setManufacturerName(
                resolveStringField(
                        "manufacturerName",
                        sources,
                        fieldResults
                )
        );

        result.setBrandName(
                resolveStringField(
                        "brandName",
                        sources,
                        fieldResults
                )
        );

        result.setManufacturerPartNumber(
                resolveStringField(
                        "manufacturerPartNumber",
                        sources,
                        fieldResults
                )
        );

        result.setShortDescription(
                resolveStringField(
                        "shortDescription",
                        sources,
                        fieldResults
                )
        );
        if (result.getName() == null || result.getName().isBlank()) {
    result.setName(result.getShortDescription());
}

        result.setLongDescription(
                resolveStringField(
                        "longDescription",
                        sources,
                        fieldResults
                )
        );

        result.setWarranty(
                resolveStringField(
                        "warranty",
                        sources,
                        fieldResults
                )
        );

        result.setProductImage(
                resolveStringField(
                        "productImage",
                        sources,
                        fieldResults
                )
        );

        result.setSpecificationSheet(
                resolveStringField(
                        "specificationSheet",
                        sources,
                        fieldResults
                )
        );

        /*
         * Attach the complete field resolution information
         * to the final DTO.
         */
        result.setFieldResults(fieldResults);

        return result;
    }

    private String resolveStringField(
            String fieldName,
            List<ExtractedSource> sources,
            Map<String, FieldResultDTO> fieldResults) {

        List<FieldResultDTO> candidates =
                new ArrayList<>();

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

        FieldResultDTO resolved =
                conflictResolverService.resolve(
                        fieldName,
                        candidates
                );

        /*
         * Store complete resolution metadata.
         */
        fieldResults.put(
                fieldName,
                resolved
        );

        return resolved.getValue();
    }

    private Double resolveDoubleField(
            String fieldName,
            List<ExtractedSource> sources,
            Map<String, FieldResultDTO> fieldResults) {

        List<FieldResultDTO> candidates =
                new ArrayList<>();

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

        FieldResultDTO resolved =
                conflictResolverService.resolve(
                        fieldName,
                        candidates
                );

        /*
         * Store complete resolution metadata.
         */
        fieldResults.put(
                fieldName,
                resolved
        );

        return Double.valueOf(
                resolved.getValue()
        );
    }

    private FieldResultDTO createCandidate(
            String fieldName,
            String value,
            ExtractedSource source) {

        FieldResultDTO candidate =
                new FieldResultDTO();

        candidate.setFieldName(fieldName);
        candidate.setValue(value);

        candidate.setSource(
                source.source()
        );

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

            case "name" ->
                    product.getName();

            case "category" ->
                    product.getCategory();

            case "material" ->
                    product.getMaterial();

            case "dimensions" ->
                    product.getDimensions();

            case "manufacturerName" ->
                    product.getManufacturerName();

            case "brandName" ->
                    product.getBrandName();

            case "manufacturerPartNumber" ->
                    product.getManufacturerPartNumber();

            case "shortDescription" ->
                    product.getShortDescription();

            case "longDescription" ->
                    product.getLongDescription();

            case "warranty" ->
                    product.getWarranty();

            case "productImage" ->
                    product.getProductImage();

            case "specificationSheet" ->
                    product.getSpecificationSheet();

            default -> null;
        };
    }

    private Double getDoubleValue(
            String fieldName,
            ProductSchemaDTO product) {

        return switch (fieldName) {

            case "price" ->
                    product.getPrice();

            case "weight" ->
                    product.getWeight();

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