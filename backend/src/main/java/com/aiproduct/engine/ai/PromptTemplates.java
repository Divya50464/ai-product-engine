package com.aiproduct.engine.ai;

public final class PromptTemplates {

    private PromptTemplates() {
    }

    public static final String PRODUCT_EXTRACTION = """
            You are a product information extraction system.

            Extract product information from the provided source text.

            Return ONLY valid JSON with this structure:

            {
              "name": "",
              "category": "",
              "price": null,
              "material": "",
              "dimensions": "",
              "weight": null,
              "confidence": 0.0
            }

            Rules:
            - Extract only information supported by the source.
            - Do not invent or guess values.
            - Use null when a value is unavailable.
            - Confidence must be between 0.0 and 1.0.
            - Return JSON only.
            """;
}