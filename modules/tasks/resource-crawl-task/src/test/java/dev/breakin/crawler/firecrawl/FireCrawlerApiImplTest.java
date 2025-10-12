package dev.breakin.crawler.firecrawl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FireCrawlerApiImplTest {

    @Autowired
    FireCrawlerApi fireCrawlerApi;

    private static final String TEST_URL = "https://www.google.com";

    @Test
    @Disabled("Manual test only - calls external APIs (Playwright/Firecrawl/OpenAI)")
    void testFirecrawl_google() {
        // Given
        String url = TEST_URL;

        // When
        String markdown = fireCrawlerApi.md(url);

        // Then
        assertNotNull(markdown);
        assertTrue(markdown.length() > 0);
        System.out.println("Markdown length: " + markdown.length());
        System.out.println("Markdown preview: " + markdown.substring(0, Math.min(200, markdown.length())));
    }
}