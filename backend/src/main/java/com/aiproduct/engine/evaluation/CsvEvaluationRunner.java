package com.aiproduct.engine.evaluation;

import com.aiproduct.engine.dto.ProductSchemaDTO;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class CsvEvaluationRunner {

    private final CsvEvaluationService csvEvaluationService;

    public CsvEvaluationRunner(
            CsvEvaluationService csvEvaluationService) {
        this.csvEvaluationService = csvEvaluationService;
    }

    public String runEvaluation() throws Exception {

        List<ProductSchemaDTO> actualProducts =
                csvEvaluationService.assembleFromCsv(
                        "data/input/evaluation_input.csv"
                );

        List<String[]> expectedRows = readExpectedFile();

        int totalFields = 0;
        int correctFields = 0;

        StringBuilder report = new StringBuilder();

        report.append("\n=================================\n");
        report.append("CSV EVALUATION RESULT\n");
        report.append("=================================\n");

        int count = Math.min(
                actualProducts.size(),
                expectedRows.size()
        );

        for (int i = 0; i < count; i++) {

            ProductSchemaDTO actual = actualProducts.get(i);
            String[] expected = expectedRows.get(i);

            report.append("\nProduct ").append(i + 1).append("\n");

            // 1. Manufacturer Part Number
            correctFields++;
            totalFields++;

            if (same(
                    actual.getManufacturerPartNumber(),
                    expected[0])) {

                report.append("manufacturerPartNumber : PASS\n");

            } else {

                correctFields--;
                report.append("manufacturerPartNumber : FAIL\n");
                report.append("  Expected: ")
                        .append(expected[0]).append("\n");
                report.append("  Actual: ")
                        .append(actual.getManufacturerPartNumber())
                        .append("\n");
            }

            // 2. Brand
            correctFields++;
            totalFields++;

            if (same(
                    actual.getBrandName(),
                    expected[1])) {

                report.append("brandName : PASS\n");

            } else {

                correctFields--;
                report.append("brandName : FAIL\n");
                report.append("  Expected: ")
                        .append(expected[1]).append("\n");
                report.append("  Actual: ")
                        .append(actual.getBrandName()).append("\n");
            }

            // 3. Manufacturer
            correctFields++;
            totalFields++;

            if (same(
                    actual.getManufacturerName(),
                    expected[2])) {

                report.append("manufacturerName : PASS\n");

            } else {

                correctFields--;
                report.append("manufacturerName : FAIL\n");
                report.append("  Expected: ")
                        .append(expected[2]).append("\n");
                report.append("  Actual: ")
                        .append(actual.getManufacturerName()).append("\n");
            }

            // 4. Name
            correctFields++;
            totalFields++;

            if (same(
                    actual.getName(),
                    expected[3])) {

                report.append("name : PASS\n");

            } else {

                correctFields--;
                report.append("name : FAIL\n");
                report.append("  Expected: ")
                        .append(expected[3]).append("\n");
                report.append("  Actual: ")
                        .append(actual.getName()).append("\n");
            }

            // 5. Short description
            correctFields++;
            totalFields++;

            if (same(
                    actual.getShortDescription(),
                    expected[4])) {

                report.append("shortDescription : PASS\n");

            } else {

                correctFields--;
                report.append("shortDescription : FAIL\n");
                report.append("  Expected: ")
                        .append(expected[4]).append("\n");
                report.append("  Actual: ")
                        .append(actual.getShortDescription()).append("\n");
            }
        }

        double accuracy = totalFields == 0
                ? 0
                : (correctFields * 100.0) / totalFields;

        report.append("\n=================================\n");
        report.append("TOTAL FIELDS : ")
                .append(totalFields).append("\n");

        report.append("CORRECT      : ")
                .append(correctFields).append("\n");

        report.append("INCORRECT    : ")
                .append(totalFields - correctFields).append("\n");

        report.append("ACCURACY     : ")
                .append(String.format("%.2f", accuracy))
                .append("%\n");

        report.append("=================================\n");

        return report.toString();
    }

    private List<String[]> readExpectedFile()
            throws Exception {

        try (BufferedReader reader =
                     Files.newBufferedReader(
                             Path.of(
                                     "data/output/expected_evaluation.csv"
                             ))) {

            reader.readLine(); // skip header

            return reader.lines()
                    .filter(line -> !line.isBlank())
                    .map(line -> line.split(",", -1))
                    .toList();
        }
    }

    private boolean same(String actual, String expected) {

    if (actual == null && expected == null) {
        return true;
    }

    if (actual == null || expected == null) {
        return false;
    }

    String normalizedActual = normalize(actual);
    String normalizedExpected = normalize(expected);

    return normalizedActual.equals(normalizedExpected);
}

private String normalize(String value) {

    return value
            .trim()
            .replace("\"", "")
            .replaceAll("\\s+", " ")
            .replaceAll("[.!?]+$", "");
}

}