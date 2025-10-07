package dev.breakin.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Multi-Module Spring Boot Application
 *
 * 컴포넌트 스캔 설정은 application.config.ModuleScanConfig에서 중앙 관리됩니다.
 */
@SpringBootApplication(
    scanBasePackages = "dev.breakin.application.config"
)
public class BreakinApplication {

    public static void main(String[] args) {
        SpringApplication.run(BreakinApplication.class, args);
    }
}
