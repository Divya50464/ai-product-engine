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
  "manufacturerName": "",
  "brandName": "",
  "manufacturerPartNumber": "",
  "shortDescription": "",
  "longDescription": "",
  "warranty": "",
  "productImage": "",
  "specificationSheet": ""
}

Rules:
- Extract only information explicitly supported by the source.
- Do not invent or guess values.
- Use null when a value is unavailable.
- For String fields, use an empty string when unavailable.
- price must be a number only, without currency symbols or commas.
- weight must be a number only, without units.
- For example, ₹79,900 must be returned as 79900.
- For example, 170 g must be returned as 170.
- manufacturerName means the manufacturer's/company name.
- brandName means the product brand.
- manufacturerPartNumber means the manufacturer's part number.
- shortDescription should be a concise product description supported by the source.
- longDescription should contain a more detailed description supported by the source.
- warranty should contain the warranty information exactly as supported by the source.
- productImage should contain an image URL or image filename only if provided by the source.
- specificationSheet should contain a specification-sheet URL or filename only if provided by the source.
- Do not include confidence, explanation, source, metadata, or any other fields.
- Return JSON only.
""";
}