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

        FieldResultDTO winner = candidates.stream()
                .filter(this::isValidCandidate)
                .min(this::compareCandidates)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No valid candidate found"
                        )
                );

        FieldResultDTO rejected = candidates.stream()
                .filter(candidate -> candidate != winner)
                .filter(this::isValidCandidate)
                .max(this::compareCandidates)
                .orElse(null);

        winner.setFieldName(fieldName);
        winner.setStatus("VERIFIED");

        if (rejected != null) {
            winner.setRejectedValue(rejected.getValue());
            winner.setRejectedSource(rejected.getSource());
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

        int authorityComparison =
                Integer.compare(
                        a.getAuthorityTier(),
                        b.getAuthorityTier()
                );

        if (authorityComparison != 0) {
            return authorityComparison;
        }

        return Double.compare(
                b.getConfidence(),
                a.getConfidence()
        );
    }
}