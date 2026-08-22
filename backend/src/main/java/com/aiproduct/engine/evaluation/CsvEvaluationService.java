package com.aiproduct.engine.evaluation;

import com.aiproduct.engine.dto.ProductSchemaDTO;
import com.aiproduct.engine.service.ProductAssemblyService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvEvaluationService {

    private final ProductAssemblyService productAssemblyService;

    public CsvEvaluationService(
            ProductAssemblyService productAssemblyService) {
        this.productAssemblyService = productAssemblyService;
    }

    public List<ProductSchemaDTO> assembleFromCsv(String inputPath)
            throws IOException {

        List<ProductSchemaDTO> products = new ArrayList<>();

        try (BufferedReader reader =
                     Files.newBufferedReader(Path.of(inputPath))) {

            String header = reader.readLine();

            if (header == null) {
                return products;
            }

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] columns = line.split(",", -1);

                if (columns.length < 6) {
                    continue;
                }

                String partNumber = clean(columns[0]);
                String description = clean(columns[1]);
                String brand = clean(columns[2]);
                String manufacturer = clean(columns[5]);

                String sourceText =
                        "Manufacturer Part Number: " + partNumber
                        + ". Product Description: " + description
                        + ". Brand: " + brand
                        + ". Manufacturer: " + manufacturer
                        + ".";

                ProductAssemblyService.SourceInput source =
                        new ProductAssemblyService.SourceInput(
                                manufacturer,
                                2,
                                sourceText
                        );

                ProductSchemaDTO product =
                        productAssemblyService.assemble(
                                List.of(source)
                        );

                products.add(product);

                
            }
        }

        System.out.println(
                "TOTAL PRODUCTS ASSEMBLED: " + products.size()
        );

        return products;
    }

    private String clean(String value) {

        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.equals("--")
                || value.equals("-")
                || value.equals("\"\"")
                || value.isBlank()) {
            return null;
        }

        return value.replace("\"", "");
    }
}
