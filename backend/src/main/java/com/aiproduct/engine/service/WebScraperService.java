package com.aiproduct.engine.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebScraperService {

    public String extractText(String url) {
        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            return document.body().text();

        } catch (IOException e) {
            throw new RuntimeException("Failed to scrape the website: " + url, e);
        }
    }
}