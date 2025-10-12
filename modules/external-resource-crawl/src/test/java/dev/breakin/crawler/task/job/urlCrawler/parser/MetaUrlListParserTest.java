package dev.breakin.crawler.batch.job.urlCrawler.parser;

import dev.breakin.crawler.playwright.PlaywrightApi;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MetaUrlListParserTest {
    String metaList = "https://www.metacareers.com/jobs?q=software%20engineer&sort_by_new=true";

    @Autowired
    MetaUrlListParser metaUrlListParser;

    @Autowired
    PlaywrightApi playwrightApi;


    @Test
    @Disabled("Manual test only - calls external APIs (Playwright/Firecrawl/OpenAI)")
    void parse() throws InterruptedException {

        var html = playwrightApi.waitAndGetHtml(metaList, 10);

        var parsed = metaUrlListParser.extractUrls(html);

        assertTrue(parsed.size() > 0);
    }
}