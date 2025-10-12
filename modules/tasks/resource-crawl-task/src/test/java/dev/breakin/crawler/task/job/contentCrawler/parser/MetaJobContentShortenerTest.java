package dev.breakin.crawler.task.job.contentCrawler.parser;

import dev.breakin.crawler.firecrawl.FireCrawlerApi;
import dev.breakin.openai.base.GptParams;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class MetaJobContentShortenerTest {
    String url = "https://www.metacareers.com/jobs/1436181490732782";


    @Autowired
    FireCrawlerApi fireCrawlerApi;
    @Autowired
    MetaJobContentShortener metaJobContentShortener;

    @Test
    @Disabled("Manual test only - calls external APIs (Playwright/Firecrawl/OpenAI)")
    void extract(){
        var md = fireCrawlerApi.md(url);
        var shortened = metaJobContentShortener.run(GptParams.ofMini(md));
        assertNotNull(md);
    }
}