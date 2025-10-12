package dev.breakin.crawler.batch.job.contentGenerator.contentGenerator;

import dev.breakin.crawler.batch.job.contentCrawler.parser.GoogleJobContentShortener;
import dev.breakin.crawler.batch.job.contentCrawler.parser.MetaJobContentShortener;
import dev.breakin.crawler.batch.job.urlCrawler.parser.GoogleUrlListParser;
import dev.breakin.crawler.batch.job.urlCrawler.parser.MetaUrlListParser;
import dev.breakin.crawler.firecrawl.FireCrawlerApi;
import dev.breakin.crawler.playwright.PlaywrightApi;
import dev.breakin.model.common.Company;
import dev.breakin.model.job.Job;
import dev.breakin.openai.base.GptParams;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ContentGenerateFacadeTest {

    private static final String GOOGLE_LIST_URL = "https://www.google.com/about/careers/applications/jobs/results?sort_by=date&q=%22Software%20Engineer%22";
    private static final String META_LIST_URL = "https://www.metacareers.com/jobs?q=software%20engineer&sort_by_new=true";

    @Autowired
    PlaywrightApi playwrightApi;

    @Autowired
    FireCrawlerApi fireCrawlerApi;

    @Autowired
    GoogleUrlListParser googleUrlListParser;

    @Autowired
    MetaUrlListParser metaUrlListParser;

    @Autowired
    GoogleJobContentShortener googleJobContentShortener;

    @Autowired
    MetaJobContentShortener metaJobContentShortener;

    @Autowired
    ContentGenerateFacade contentGenerateFacade;

    @Test
    //@Disabled("Manual test only - calls external APIs (Playwright/Firecrawl/OpenAI)")
    void testGoogleJobGeneration() throws InterruptedException {
        // Step 1: Parse URL list to get first job URL
        String html = playwrightApi.waitAndGetHtml(GOOGLE_LIST_URL, 10);
        var urlList = googleUrlListParser.extractUrls(html);
        assertTrue(urlList.size() > 0, "Should have at least one job URL");

        var firstJob = urlList.get(0);
        System.out.println("Testing with URL: " + firstJob.url());
        System.out.println("Job title: " + firstJob.title());

        // Step 2: Crawl content and shorten
        String markdown = fireCrawlerApi.md(firstJob.url());
        String shortened = googleJobContentShortener.run(GptParams.ofMini(markdown));
        assertNotNull(shortened, "Shortened content should not be null");

        // Step 3: Generate Job object
        Job job = contentGenerateFacade.generate(
                shortened,
                firstJob.url(),
                firstJob.title(),
                Company.GOOGLE
        );

        // Assertions
        assertNotNull(job);
        assertEquals(firstJob.url(), job.getUrl());
        assertEquals(Company.GOOGLE, job.getCompany());
        assertEquals(firstJob.title(), job.getTitle());
        assertNotNull(job.getOneLineSummary());

        System.out.println("Generated Job: " + job);
    }

    @Test
    @Disabled("Manual test only - calls external APIs (Playwright/Firecrawl/OpenAI)")
    void testMetaJobGeneration() throws InterruptedException {
        // Step 1: Parse URL list to get first job URL
        String html = playwrightApi.waitAndGetHtml(META_LIST_URL, 10);
        var urlList = metaUrlListParser.extractUrls(html);
        assertTrue(urlList.size() > 0, "Should have at least one job URL");

        var firstJob = urlList.get(0);
        System.out.println("Testing with URL: " + firstJob.url());
        System.out.println("Job title: " + firstJob.title());

        // Step 2: Crawl content and shorten
        String markdown = fireCrawlerApi.md(firstJob.url());
        String shortened = metaJobContentShortener.run(GptParams.ofMini(markdown));
        assertNotNull(shortened, "Shortened content should not be null");

        // Step 3: Generate Job object
        Job job = contentGenerateFacade.generate(
                shortened,
                firstJob.url(),
                firstJob.title(),
                Company.META
        );

        // Assertions
        assertNotNull(job);

        assertEquals(firstJob.url(), job.getUrl());
        assertEquals(Company.META, job.getCompany());
        assertEquals(firstJob.title(), job.getTitle());
        assertNotNull(job.getOneLineSummary());

        System.out.println("Generated Job: " + job);
    }

    @Test
    @Disabled("Manual test only - calls external APIs (Playwright/Firecrawl/OpenAI)")
    void testGoogleJobGenerationMultiple() throws InterruptedException {
        // Step 1: Parse URL list
        String html = playwrightApi.waitAndGetHtml(GOOGLE_LIST_URL, 10);
        var urlList = googleUrlListParser.extractUrls(html);
        assertTrue(urlList.size() > 0, "Should have at least one job URL");

        System.out.println("Total Google jobs found: " + urlList.size());

        // Step 2: Process all jobs
        List<Job> generatedJobs = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        for (int i = 0; i < urlList.size(); i++) {
            var jobUrl = urlList.get(i);
            System.out.println(String.format("[%d/%d] Processing: %s", i + 1, urlList.size(), jobUrl.title()));

            try {
                // Crawl content and shorten
                String markdown = fireCrawlerApi.md(jobUrl.url());
                String shortened = googleJobContentShortener.run(GptParams.ofMini(markdown));

                // Generate Job object
                Job job = contentGenerateFacade.generate(
                        shortened,
                        jobUrl.url(),
                        jobUrl.title(),
                        Company.GOOGLE
                );

                generatedJobs.add(job);
                successCount++;
                System.out.println("  ✓ Success");
            } catch (Exception e) {
                failCount++;
                System.err.println("  ✗ Failed: " + e.getMessage());
            }
        }

        // Summary
        System.out.println("\n=== Summary ===");
        System.out.println("Total: " + urlList.size());
        System.out.println("Success: " + successCount);
        System.out.println("Failed: " + failCount);
        System.out.println("Generated jobs: " + generatedJobs.size());

        assertTrue(successCount > 0, "At least one job should be generated successfully");
    }

    @Test
   // @Disabled("Manual test only - calls external APIs (Playwright/Firecrawl/OpenAI)")
    void testMetaJobGenerationMultiple() throws InterruptedException {
        // Step 1: Parse URL list
        String html = playwrightApi.waitAndGetHtml(META_LIST_URL, 10);
        var urlList = metaUrlListParser.extractUrls(html);
        assertTrue(urlList.size() > 0, "Should have at least one job URL");

        System.out.println("Total Meta jobs found: " + urlList.size());

        // Step 2: Process all jobs
        List<Job> generatedJobs = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        for (int i = 0; i < urlList.size(); i++) {
            var jobUrl = urlList.get(i);
            System.out.println(String.format("[%d/%d] Processing: %s", i + 1, urlList.size(), jobUrl.title()));

            try {
                // Crawl content and shorten
                String markdown = fireCrawlerApi.md(jobUrl.url());
                String shortened = metaJobContentShortener.run(GptParams.ofMini(markdown));

                // Generate Job object
                Job job = contentGenerateFacade.generate(
                        shortened,
                        jobUrl.url(),
                        jobUrl.title(),
                        Company.META
                );

                generatedJobs.add(job);
                successCount++;
                System.out.println("  ✓ Success");
            } catch (Exception e) {
                failCount++;
                System.err.println("  ✗ Failed: " + e.getMessage());
            }
        }

        // Summary
        System.out.println("\n=== Summary ===");
        System.out.println("Total: " + urlList.size());
        System.out.println("Success: " + successCount);
        System.out.println("Failed: " + failCount);
        System.out.println("Generated jobs: " + generatedJobs.size());

        assertTrue(successCount > 0, "At least one job should be generated successfully");
    }

}