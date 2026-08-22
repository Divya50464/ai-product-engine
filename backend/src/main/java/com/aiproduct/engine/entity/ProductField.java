package com.aiproduct.engine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_field")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "field_name")
    private String fieldName;

    @Column(length = 1000)
    private String value;

    private Double confidence;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority_tier")
    private AuthorityTier authorityTier;

    private String source;

    @Column(length = 2000)
    private String evidence;

    @Column(name = "rejected_value", length = 1000)
    private String rejectedValue;

    @Column(name = "rejected_source")
    private String rejectedSource;

    public enum AuthorityTier {
        MANUFACTURER_SPEC,
        CATALOG_PDF,
        WEBSITE,
        INFERRED
    }
}