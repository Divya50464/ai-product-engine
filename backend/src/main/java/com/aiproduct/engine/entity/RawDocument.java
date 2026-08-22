package com.aiproduct.engine.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "raw_document")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type")
    private SourceType sourceType;

    @Column(name = "raw_text", columnDefinition = "LONGTEXT")
    private String rawText;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_authority_tier")
    private ProductField.AuthorityTier sourceAuthorityTier;

    @Column(name = "source_reference")
    private String sourceReference;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum SourceType {
        PDF,
        URL
    }
}