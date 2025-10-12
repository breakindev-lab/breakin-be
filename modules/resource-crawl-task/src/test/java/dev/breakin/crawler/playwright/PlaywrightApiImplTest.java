package dev.breakin.crawler.playwright;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PlaywrightApiImplTest {

    @Autowired
    PlaywrightApi playwrightApi;

    private static final String TEST_URL = "https://www.google.com";

    @Test
    @Disabled("Manual test only - calls external APIs (Playwright/Firecrawl/OpenAI)")
    void testPlaywright_google() throws InterruptedException {
        // Given
        String url = TEST_URL;
        int waitSeconds = 3;

        // When
        String html = playwrightApi.waitAndGetHtml(url, waitSeconds);

        // Then
        assertNotNull(html);
        assertTrue(html.length() > 0);
        assertTrue(html.toLowerCase().contains("google"));
        System.out.println("HTML length: " + html.length());
        System.out.println("HTML preview: " + html.substring(0, Math.min(200, html.length())));
    }
}