package com.aiproduct.engine.repository;

import com.aiproduct.engine.entity.RawDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawDocumentRepository extends JpaRepository<RawDocument, Long> {
}