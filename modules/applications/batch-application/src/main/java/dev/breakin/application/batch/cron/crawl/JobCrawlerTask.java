package dev.breakin.application.batch.cron.crawl;

import dev.breakin.crawler.task.job.contentCrawler.JobContentCrawler;
import dev.breakin.crawler.task.job.contentGenerator.JobContentGenerator;
import dev.breakin.crawler.task.job.urlCrawler.GoogleJobCrawler;
import dev.breakin.crawler.task.job.urlCrawler.MetaJobCrawler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

import static dev.breakin.application.batch.ScheduleUtils.executeBatchTask;

@RequiredArgsConstructor
@Component
@Slf4j
public class JobCrawlerTask {

    private final MetaJobCrawler metaJobCrawler;
    private final GoogleJobCrawler googleJobCrawler;
    private final JobContentCrawler jobContentCrawler;
    private final JobContentGenerator jobContentGenerator;

    private static final AtomicBoolean URL_CRAWL_RUNNING = new AtomicBoolean(false);
    private static final AtomicBoolean CONTENT_CRAWL_RUNNING = new AtomicBoolean(false);
    private static final AtomicBoolean JOB_GENERATE_RUNNING = new AtomicBoolean(false);

    /**
     * URL 수집: 매일 새벽 2시에 실행
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void crawlJobUrls() {
        executeBatchTask(URL_CRAWL_RUNNING, "crawl_job_urls", () -> {
            log.info("Starting URL crawling for Meta");
            metaJobCrawler.run();

            log.info("Starting URL crawling for Google");
            googleJobCrawler.run();

            log.info("URL crawling completed");
        });
    }

    /**
     * 콘텐츠 크롤링: 5초마다 실행
     */
    @Scheduled(fixedDelay = 5000)
    public void crawlJobContent() {
        executeBatchTask(CONTENT_CRAWL_RUNNING, "crawl_job_content", jobContentCrawler::run);
    }

    /**
     * Job 생성: 5초마다 실행
     */
    @Scheduled(fixedDelay = 5000)
    public void generateJob() {
        executeBatchTask(JOB_GENERATE_RUNNING, "generate_job", jobContentGenerator::run);
    }
}
