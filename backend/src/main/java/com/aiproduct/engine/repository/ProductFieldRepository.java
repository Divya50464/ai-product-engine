package com.aiproduct.engine.repository;

import com.aiproduct.engine.entity.ProductField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductFieldRepository extends JpaRepository<ProductField, Long> {
    List<ProductField> findByProductId(Long productId);
}