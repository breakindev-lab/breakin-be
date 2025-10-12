package dev.breakin.application.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Batch Application
 *
 * 모든 배치 모듈의 @Scheduled 메서드를 스캔하고 실행합니다.
 * - external-resource-crawl: 외부 리소스 크롤링
 * - (미래에 추가될 다른 배치 모듈들...)
 */
@SpringBootApplication(scanBasePackages = {
        "dev.breakin.crawler",           // external-resource-crawl 모듈
        "dev.breakin.application.batch"  // 현재 모듈
})
@EnableScheduling
public class ApplicationBatch {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationBatch.class, args);
    }
}
