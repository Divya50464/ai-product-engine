package com.aiproduct.engine.service;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DocumentParserService {

    private final Tika tika = new Tika();

    public String extractText(MultipartFile file) {
        try {
            return tika.parseToString(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the uploaded file", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract text from the document", e);
        }
    }
}