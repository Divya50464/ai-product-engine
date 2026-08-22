package com.aiproduct.engine.controller;
import com.aiproduct.engine.evaluation.CsvEvaluationRunner;
import com.aiproduct.engine.evaluation.CsvEvaluationService;
import com.aiproduct.engine.dto.ProductSchemaDTO;
import com.aiproduct.engine.entity.Product;
import com.aiproduct.engine.repository.ProductRepository;
import com.aiproduct.engine.service.ProductAssemblyService;
import com.aiproduct.engine.service.ProductPersistenceService;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductAssemblyService productAssemblyService;
    private final ProductPersistenceService productPersistenceService;
    private final CsvEvaluationService csvEvaluationService;
    private final CsvEvaluationRunner csvEvaluationRunner;

   public ProductController(
        ProductRepository productRepository,
        ProductAssemblyService productAssemblyService,
        ProductPersistenceService productPersistenceService,
        CsvEvaluationService csvEvaluationService, CsvEvaluationRunner csvEvaluationRunner) {

    this.productRepository = productRepository;
    this.productAssemblyService = productAssemblyService;
    this.productPersistenceService = productPersistenceService;
    this.csvEvaluationService = csvEvaluationService;
    this.csvEvaluationRunner = csvEvaluationRunner;
}

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(
            @PathVariable Long id) {

        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

        @PostMapping("/assemble")
    public ResponseEntity<Product> assembleAndSave(
            @RequestBody List<ProductAssemblyService.SourceInput> sources) {

        // 1. Extract + resolve conflicts
        ProductSchemaDTO assembledProduct =
                productAssemblyService.assemble(sources);

        // 2. Save final product into MySQL
        Product savedProduct =
                productPersistenceService.saveProduct(
                        assembledProduct,
                        "AI_ASSEMBLED"
                );

        return ResponseEntity.ok(savedProduct);
    }


    @PostMapping("/evaluate-csv")
public ResponseEntity<List<ProductSchemaDTO>> evaluateCsv()
        throws Exception {

    List<ProductSchemaDTO> products =
            csvEvaluationService.assembleFromCsv(
                    "data/input/evaluation_input.csv"
            );

    return ResponseEntity.ok(products);
}   // ← THIS closes evaluateCsv()

@PostMapping("/evaluate-accuracy")
public ResponseEntity<String> evaluateAccuracy()
        throws Exception {

    return ResponseEntity.ok(
            csvEvaluationRunner.runEvaluation()
    );
}   // ← closes evaluateAccuracy
    }

