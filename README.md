# AI Product Intelligence Engine

An AI-powered product intelligence system that extracts, evaluates, and resolves product information from multiple sources.

The system compares information from sources such as manufacturer documents and websites, detects conflicting values, evaluates source authority and confidence, and produces a final consolidated product record.

---

## System Workflow

```text
Multiple Product Sources
          |
          v
+-------------------------+
|   Product Extraction    |
|       using AI          |
+-------------------------+
          |
          v
+-------------------------+
|   Conflict Detection    |
+-------------------------+
          |
          v
+-------------------------+
| Authority + Confidence  |
|       Evaluation        |
+-------------------------+
          |
          v
+-------------------------+
|   Final Product Data    |
+-------------------------+
          |
          +-------------------+
          |                   |
          v                   v
        MySQL              REST API
                              |
                              v
                        Web Dashboard

Key Features
AI-based product information extraction
Multi-source product comparison
Conflict detection between sources
Source authority evaluation
Confidence scoring
Higher-authority source selection
Rejected value tracking
Product persistence using MySQL
REST APIs using Spring Boot
CSV-based evaluation
Accuracy evaluation
Web-based product dashboard
Final product JSON visualization

---

## Conflict Detection and Resolution

The system compares information from multiple sources and identifies conflicting values.

For example:

```text
Manufacturer PDF
Weight: 2 kg
Authority Tier: 1

Website
Weight: 5 kg
Authority Tier: 2
```

The system selects the value from the higher-authority source.

```text
Selected Value : 2 kg
Rejected Value : 5 kg
Selected Source: Manufacturer PDF
```

This allows the system to maintain the final product data while also preserving information about conflicting sources.

---

## Authority Tiers

The system uses source authority to determine which value should be preferred when multiple sources contain different values.

Example:

```text
Tier 1
Manufacturer Specification / Manufacturer PDF

Tier 2
Website

Tier 3
Lower-authority or secondary sources
```

A source with a higher authority is preferred when resolving conflicts.

---

## Confidence

Each extracted field can contain a confidence score.

Example:

```text
Material
Value       : Stainless Steel
Confidence  : 0.92
Source      : Manufacturer PDF
Authority   : MANUFACTURER_SPEC
```

The confidence score represents how confident the extraction system is about the selected value.

---

## Example Product

```text
Product Name : Industrial Pressure Sensor
Category     : Pressure Sensor
Price        : 100.0
Material     : Stainless Steel
Weight       : 2.0 kg
```

Example conflicting information:

```text
Manufacturer PDF:
Weight = 2 kg
Authority = Tier 1

Website:
Weight = 5 kg
Authority = Tier 2
```

The manufacturer PDF is selected because it has higher source authority.

---

## Final Product Data

The final product stores both the selected value and information about the rejected conflicting value.

Example:

```json
{
  "name": "Industrial Pressure Sensor",
  "category": "Pressure Sensor",
  "fields": [
    {
      "fieldName": "weight",
      "value": "2.0",
      "confidence": 0.92,
      "authorityTier": "MANUFACTURER_SPEC",
      "source": "Manufacturer PDF",
      "rejectedValue": "5.0",
      "rejectedSource": "Website"
    }
  ]
}
```

---

## Technology Stack

### Backend

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- Maven
- REST API

### AI

- Google Gemini
- Spring AI

### Database

- MySQL

### Frontend

- HTML
- CSS
- JavaScript

### Evaluation

- CSV-based evaluation
- Product extraction evaluation
- Accuracy evaluation

---

## Project Structure

```text
ai-product-engine/
|
+-- backend/
|   |
|   +-- src/
|   |   +-- main/
|   |       +-- java/
|   |       +-- resources/
|   |
|   +-- pom.xml
|
+-- frontend/
|   |
|   +-- dashboard.html
|
+-- data/
|   |
|   +-- input/
|       +-- evaluation_input.csv
|
+-- docs/
|
+-- README.md
```

---

## REST API

The backend exposes REST APIs under:

```text
/api/products
```

### Get All Products

```http
GET /api/products
```

### Get Product by ID

```http
GET /api/products/{id}
```

### Assemble Product

```http
POST /api/products/assemble
```

Extracts and assembles product information from multiple sources, resolves conflicts, and saves the final product.

### Evaluate CSV

```http
POST /api/products/evaluate-csv
```

### Evaluate Accuracy

```http
POST /api/products/evaluate-accuracy
```

---

## Web Dashboard

The project includes a web dashboard for viewing the final product information.

The dashboard displays:

- Product information
- Product category
- Price
- Material
- Weight
- Confidence scores
- Source information
- Conflict detection
- Selected value
- Rejected value
- Conflict resolution reason
- Final product JSON visualization

The dashboard communicates with the Spring Boot backend through the REST API.

---

## Running the Backend

Navigate to the backend directory:

```bash
cd backend
```

Run the Spring Boot application:

```bash
mvn spring-boot:run
```

The backend starts on:

```text
http://localhost:8080
```

---

## Running the Frontend

The frontend dashboard is located at:

```text
frontend/dashboard.html
```

The dashboard can be opened in a web browser.

Make sure the Spring Boot backend is running on port `8080` before loading the dashboard.

---

## Database

The application uses MySQL for storing assembled product information.

The main entities include:

```text
Product
ProductField
```

A product contains multiple product fields.

```text
Product
 |
 +-- price
 |
 +-- material
 |
 +-- weight
```

Each product field can store:

- Field name
- Value
- Confidence
- Authority tier
- Source
- Evidence
- Rejected value
- Rejected source

---

## Evaluation

The project includes CSV-based evaluation.

The evaluation process can be used to test:

- Product extraction
- Conflict resolution
- Source selection
- Confidence
- Accuracy

The evaluation input is stored under:

```text
data/input/evaluation_input.csv
```

Evaluation can be triggered through:

```http
POST /api/products/evaluate-csv
```

and:

```http
POST /api/products/evaluate-accuracy
```

---

## MVP Status

The current MVP demonstrates the complete basic workflow:

```text
Input Sources
      |
      v
AI Extraction
      |
      v
Conflict Detection
      |
      v
Authority Evaluation
      |
      v
Final Product
      |
      +---------> MySQL
      |
      +---------> REST API
      |
      +---------> Web Dashboard
```

The MVP demonstrates multi-source product analysis, conflict resolution, persistence, REST API integration, and frontend visualization.

---

## Future Improvements

Possible future improvements include:

- Automated document upload
- Support for additional document formats
- More source types
- Improved confidence scoring
- Authentication and authorization
- Better frontend visualization
- Product search and filtering
- Automated evaluation reports
- Cloud deployment
- Improved error handling and logging
- More comprehensive test coverage

---

## Team

This project was developed as a team project.

Team members contributed to different parts of the system including backend development, AI extraction, evaluation, database integration, and frontend/dashboard development.

---

## Conclusion

AI Product Intelligence Engine provides an end-to-end pipeline for extracting product information from multiple sources, detecting conflicts, evaluating source authority and confidence, and producing consolidated product data.

The current MVP connects AI-based extraction with a Spring Boot backend, MySQL persistence, REST APIs, and a web dashboard.