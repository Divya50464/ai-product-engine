package com.aiproduct.engine.controller;

import com.aiproduct.engine.entity.RawDocument;
import com.aiproduct.engine.repository.RawDocumentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ingest")
public class IngestionController {

    private final RawDocumentRepository rawDocumentRepository;

    public IngestionController(RawDocumentRepository rawDocumentRepository) {
        this.rawDocumentRepository = rawDocumentRepository;
    }

    @PostMapping("/pdf")
    public ResponseEntity<RawDocument> ingestPdf(@RequestParam("file") MultipartFile file) {
        // NOTE: actual PDF text extraction (Tika) is Member 3's DocumentParserService.
        // For now, this just proves the endpoint + DB save works end-to-end.
        RawDocument doc = new RawDocument();
        doc.setSourceType(RawDocument.SourceType.PDF);
        doc.setSourceReference(file.getOriginalFilename());
        doc.setRawText("PLACEHOLDER - Tika extraction not yet wired in");
        RawDocument saved = rawDocumentRepository.save(doc);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/url")
    public ResponseEntity<RawDocument> ingestUrl(@RequestParam("url") String url) {
        // NOTE: actual scraping (Jsoup) is Member 3's WebScraperService.
        RawDocument doc = new RawDocument();
        doc.setSourceType(RawDocument.SourceType.URL);
        doc.setSourceReference(url);
        doc.setRawText("PLACEHOLDER - Jsoup scraping not yet wired in");
        RawDocument saved = rawDocumentRepository.save(doc);
        return ResponseEntity.ok(saved);
    }
}