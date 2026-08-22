package com.aiproduct.engine.service;

import com.aiproduct.engine.dto.FieldResultDTO;
import com.aiproduct.engine.dto.ProductSchemaDTO;
import com.aiproduct.engine.entity.Product;
import com.aiproduct.engine.entity.ProductField;
import com.aiproduct.engine.repository.ProductFieldRepository;
import com.aiproduct.engine.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class ProductPersistenceService {

    private final ProductRepository productRepository;
    private final ProductFieldRepository productFieldRepository;

    public ProductPersistenceService(
            ProductRepository productRepository,
            ProductFieldRepository productFieldRepository) {

        this.productRepository = productRepository;
        this.productFieldRepository = productFieldRepository;
    }

    @Transactional
    public Product saveProduct(
            ProductSchemaDTO dto,
            String source) {

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Product data cannot be null"
            );
        }

        Product product = new Product();

        product.setName(dto.getName());
        product.setCategory(dto.getCategory());

        product = productRepository.save(product);

        /*
         * Resolved field metadata produced by
         * ProductAssemblyService.
         */
        Map<String, FieldResultDTO> fieldResults =
                dto.getFieldResults();

        saveField(
                product,
                "price",
                dto.getPrice() != null
                        ? String.valueOf(dto.getPrice())
                        : null,
                fieldResults,
                source
        );

        saveField(
                product,
                "material",
                dto.getMaterial(),
                fieldResults,
                source
        );

        saveField(
                product,
                "dimensions",
                dto.getDimensions(),
                fieldResults,
                source
        );

        saveField(
                product,
                "weight",
                dto.getWeight() != null
                        ? String.valueOf(dto.getWeight())
                        : null,
                fieldResults,
                source
        );

        saveField(
                product,
                "manufacturerName",
                dto.getManufacturerName(),
                fieldResults,
                source
        );

        saveField(
                product,
                "brandName",
                dto.getBrandName(),
                fieldResults,
                source
        );

        saveField(
                product,
                "manufacturerPartNumber",
                dto.getManufacturerPartNumber(),
                fieldResults,
                source
        );

        saveField(
                product,
                "shortDescription",
                dto.getShortDescription(),
                fieldResults,
                source
        );

        saveField(
                product,
                "longDescription",
                dto.getLongDescription(),
                fieldResults,
                source
        );

        saveField(
                product,
                "warranty",
                dto.getWarranty(),
                fieldResults,
                source
        );

        saveField(
                product,
                "productImage",
                dto.getProductImage(),
                fieldResults,
                source
        );

        saveField(
                product,
                "specificationSheet",
                dto.getSpecificationSheet(),
                fieldResults,
                source
        );

        return product;
    }

    private void saveField(
            Product product,
            String fieldName,
            String value,
            Map<String, FieldResultDTO> fieldResults,
            String fallbackSource) {

        if (value == null || value.isBlank()) {
            return;
        }

        ProductField field = new ProductField();

        field.setProduct(product);
        field.setFieldName(fieldName);
        field.setValue(value);

        /*
         * Get the resolved metadata for this field.
         */
        FieldResultDTO result =
                fieldResults != null
                        ? fieldResults.get(fieldName)
                        : null;

        if (result != null) {

            field.setConfidence(
                    result.getConfidence()
            );

            field.setAuthorityTier(
                    convertAuthorityTier(
                            result.getAuthorityTier()
                    )
            );

            field.setSource(
                    result.getSource()
            );

           

            field.setRejectedValue(
                    result.getRejectedValue()
            );

            field.setRejectedSource(
                    result.getRejectedSource()
            );

        } else {

            /*
             * Fallback for fields without resolution metadata.
             */
            field.setConfidence(0.0);

            field.setAuthorityTier(
                    ProductField.AuthorityTier.INFERRED
            );

            field.setSource(fallbackSource);
        }

        productFieldRepository.save(field);
        product.getFields().add(field);
    }

    private ProductField.AuthorityTier convertAuthorityTier(
            int authorityTier) {

        return switch (authorityTier) {
            case 1 ->
                    ProductField.AuthorityTier.MANUFACTURER_SPEC;

            case 2 ->
                    ProductField.AuthorityTier.CATALOG_PDF;

            case 3 ->
                    ProductField.AuthorityTier.WEBSITE;

            case 4 ->
                    ProductField.AuthorityTier.INFERRED;

            default ->
                    ProductField.AuthorityTier.INFERRED;
        };
    }
}