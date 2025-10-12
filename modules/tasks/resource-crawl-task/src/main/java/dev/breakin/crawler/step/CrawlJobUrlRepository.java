package dev.breakin.crawler.step;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrawlJobUrlRepository extends CrudRepository<CrawlJobUrlEntity, Long> {

    /**
     * 처리 대기 중인 첫 번째 URL 조회
     */
    @Query("SELECT * FROM crawl_job_urls WHERE status = 'WAIT' ORDER BY created_at ASC LIMIT 1")
    Optional<CrawlJobUrlEntity> findFirstWaitingUrl();

    /**
     * URL 중복 체크
     */
    boolean existsByUrl(String url);
}
