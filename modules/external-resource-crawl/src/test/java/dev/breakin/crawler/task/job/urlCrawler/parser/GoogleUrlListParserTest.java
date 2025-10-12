package dev.breakin.crawler.batch.job.urlCrawler.parser;

import dev.breakin.crawler.playwright.PlaywrightApi;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class GoogleUrlListParserTest {

    String googleList = "https://www.google.com/about/careers/applications/jobs/results?sort_by=date&q=%22Software%20Engineer%22";

    @Autowired
    GoogleUrlListParser googleUrlListParser;

    @Autowired
    PlaywrightApi playwrightApi;


    @Test
    @Disabled("Manual test only - calls external APIs (Playwright/Firecrawl/OpenAI)")
    void parse() throws InterruptedException {

        var html = playwrightApi.waitAndGetHtml(googleList, 10);

        var parsed = googleUrlListParser.extractUrls(html);

        assertTrue(parsed.size() > 0);
    }
}