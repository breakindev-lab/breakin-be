package dev.breakin.crawler.batch.job.urlCrawler;

/**
 * 채용 공고 URL과 제목을 담는 DTO
 */
public record JobUrlInfo(
        String url,
        String title
) {
}
