package dev.breakin.crawler.task.job.contentCrawler;

import dev.breakin.crawler.task.job.contentCrawler.parser.GoogleJobContentShortener;
import dev.breakin.crawler.task.job.contentCrawler.parser.MetaJobContentShortener;
import dev.breakin.crawler.firecrawl.FireCrawlerApi;
import dev.breakin.crawler.step.CrawlJobContentEntity;
import dev.breakin.crawler.step.CrawlJobContentRepository;
import dev.breakin.crawler.step.CrawlJobUrlEntity;
import dev.breakin.crawler.step.CrawlJobUrlRepository;
import dev.breakin.model.common.Company;
import dev.breakin.model.common.CrawlStatus;
import dev.breakin.openai.base.GptParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Job Content Crawler
 * <p>
 * Step 1(crawl_job_urls)에서 WAIT 상태인 URL을 하나씩 가져와서:
 * 1. Firecrawl로 Markdown 추출
 * 2. GPT로 본문 요약
 * 3. Step 2(crawl_job_contents)에 저장
 * 4. Step 1 상태를 SUCCESS로 업데이트
 * <p>
 * 회사 구분 없이 공통 로직으로 처리
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JobContentCrawler {

    private final CrawlJobUrlRepository urlRepository;
    private final CrawlJobContentRepository contentRepository;
    private final FireCrawlerApi fireCrawlerApi;


    private final GoogleJobContentShortener googleShortener;
    private final MetaJobContentShortener metaShortener;

    /**
     * 모든 WAIT 상태의 URL을 순차 처리
     */
    public void run() {
        var urlEntityOpt = urlRepository.findFirstWaitingUrl();
        if (urlEntityOpt.isEmpty()) {
            log.debug("No waiting URLs found");
            return;
        }

        CrawlJobUrlEntity urlEntity = urlEntityOpt.get();
        log.info("Processing URL: id={}, company={}, url={}",
                urlEntity.getId(), urlEntity.getCompany(), urlEntity.getUrl());

        try {
            String markdown = fireCrawlerApi.md(urlEntity.getUrl());
            log.debug("Extracted markdown: length={}", markdown.length());

            // 3. GPT로 본문 요약
            String shortened = makeShort(urlEntity.getCompany(), markdown);

            log.debug("Shortened content: length={}", shortened.length());
            CrawlJobContentEntity contentEntity = new CrawlJobContentEntity(
                    null,
                    urlEntity.getId(),
                    markdown,
                    shortened,
                    CrawlStatus.WAIT,  // Job 생성 대기 상태
                    null,
                    null,
                    Instant.now(),
                    null
            );
            contentRepository.save(contentEntity);
            log.info("Saved content: urlId={}", urlEntity.getId());

            // 5. URL 상태를 SUCCESS로 업데이트
            CrawlJobUrlEntity updatedUrl = new CrawlJobUrlEntity(
                    urlEntity.getId(),
                    urlEntity.getCompany(),
                    urlEntity.getUrl(),
                    urlEntity.getTitle(),
                    CrawlStatus.SUCCESS,
                    urlEntity.getCreatedAt(),
                    Instant.now()
            );
            urlRepository.save(updatedUrl);
            log.info("Updated URL status to SUCCESS: id={}", urlEntity.getId());
            return;
        } catch (Exception e) {
            log.error("Failed to process URL: id={}, url={}, error={}",
                    urlEntity.getId(), urlEntity.getUrl(), e.getMessage(), e);

            // URL 상태를 FAILED로 업데이트
            CrawlJobUrlEntity failedUrl = new CrawlJobUrlEntity(
                    urlEntity.getId(),
                    urlEntity.getCompany(),
                    urlEntity.getUrl(),
                    urlEntity.getTitle(),
                    CrawlStatus.FAILED,
                    urlEntity.getCreatedAt(),
                    Instant.now()
            );
            urlRepository.save(failedUrl);
            log.warn("Updated URL status to FAILED: id={}", urlEntity.getId());

            return;
        }
    }

    private String makeShort(Company company, String markdown) {
        if (company == Company.GOOGLE)
            return googleShortener.run(GptParams.ofMini(markdown));

        if (company == Company.META)
            return metaShortener.run(GptParams.ofMini(markdown));

        return markdown;
    }
}
