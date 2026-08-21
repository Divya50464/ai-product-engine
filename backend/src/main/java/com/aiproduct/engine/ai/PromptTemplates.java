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
  "weight": null
}

Rules:
- Extract only information supported by the source.
- Do not invent or guess values.
- Use null when a value is unavailable.
- price must be a number only, without currency symbols or commas.
- weight must be a number only, without units.
- For example, ₹79,900 must be returned as 79900.
- For example, 170 g must be returned as 170.
- Do not include confidence, explanation, source, metadata, or any other fields.
- Return JSON only.
""";
}