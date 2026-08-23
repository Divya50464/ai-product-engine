package com.aiproduct.engine.service;

import com.aiproduct.engine.dto.FieldResultDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConflictResolverService {

    public FieldResultDTO resolve(
            String fieldName,
            List<FieldResultDTO> candidates) {

        if (candidates == null || candidates.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one candidate is required"
            );
        }

        // Select the best candidate based on:
        // 1. Higher authority
        // 2. Higher confidence
        FieldResultDTO winner = candidates.stream()
                .filter(this::isValidCandidate)
                .min(this::compareCandidates)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No valid candidate found"
                        )
                );

        /*
         * Find a rejected candidate ONLY if its value
         * is actually different from the winning value.
         *
         * Example:
         *
         * Manufacturer PDF = 100
         * Website           = 100
         *
         * No conflict -> no rejected value.
         *
         * Manufacturer PDF = 2 kg
         * Website           = 5 kg
         *
         * Conflict -> 5 kg is rejected.
         */
        FieldResultDTO rejected = candidates.stream()
                .filter(candidate -> candidate != winner)
                .filter(this::isValidCandidate)
                .filter(candidate ->
                        !valuesEquivalent(
                                winner.getValue(),
                                candidate.getValue()
                        )
                )
                .max(this::compareCandidates)
                .orElse(null);

        winner.setFieldName(fieldName);
        winner.setStatus("VERIFIED");

        if (rejected != null) {
            winner.setRejectedValue(rejected.getValue());
            winner.setRejectedSource(rejected.getSource());
        } else {
            // No actual conflict
            winner.setRejectedValue(null);
            winner.setRejectedSource(null);
        }

        return winner;
    }

    private boolean isValidCandidate(FieldResultDTO candidate) {

        return candidate != null
                && candidate.getValue() != null
                && !candidate.getValue().isBlank()
                && candidate.getAuthorityTier() != null
                && candidate.getConfidence() != null;
    }

    private int compareCandidates(
            FieldResultDTO a,
            FieldResultDTO b) {

        // Lower authority tier number = higher authority
        int authorityComparison =
                Integer.compare(
                        a.getAuthorityTier(),
                        b.getAuthorityTier()
                );

        if (authorityComparison != 0) {
            return authorityComparison;
        }

        // Higher confidence wins
        return Double.compare(
                b.getConfidence(),
                a.getConfidence()
        );
    }

    /**
     * Checks whether two extracted values represent
     * the same value.
     *
     * Examples:
     *
     * "100.0" and "100" -> same
     * "Stainless Steel" and "Stainless Steel" -> same
     * "2.0" and "5.0" -> different
     */
    private boolean valuesEquivalent(
            String first,
            String second) {

        if (first == null || second == null) {
            return first == null && second == null;
        }

        String firstNormalized = first.trim();
        String secondNormalized = second.trim();

        // First compare as text
        if (firstNormalized.equalsIgnoreCase(secondNormalized)) {
            return true;
        }

        // Then try numeric comparison
        try {
            double firstNumber =
                    Double.parseDouble(firstNormalized);

            double secondNumber =
                    Double.parseDouble(secondNormalized);

            return Double.compare(
                    firstNumber,
                    secondNumber
            ) == 0;

        } catch (NumberFormatException e) {
            return false;
        }
    }
}