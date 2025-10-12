package dev.breakin.crawler.task.job.contentCrawler.parser;

import dev.breakin.crawler.firecrawl.FireCrawlerApi;
import dev.breakin.openai.base.GptParams;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class GoogleJobContentShortenerTest {

    String GOOGLE_JOR_DETAIL_URL1 = "https://www.google.com/about/careers/applications/jobs/results/141245305780609734-senior-software-engineer-dataproc";

    @Autowired
    FireCrawlerApi fireCrawlerApi;

    @Autowired
    GoogleJobContentShortener shortener;

    @Test
    @Disabled("Manual test only - calls external APIs (Playwright/Firecrawl/OpenAI)")
    void run(){

        var md = fireCrawlerApi.md(GOOGLE_JOR_DETAIL_URL1);
        var shortened1 = shortener.run(GptParams.ofMini(md));
        
        assert shortened1  != null;
    }
}