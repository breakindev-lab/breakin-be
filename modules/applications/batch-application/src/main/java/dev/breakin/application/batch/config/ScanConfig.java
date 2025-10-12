package dev.breakin.application.batch.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@ComponentScan(basePackages = {
        "dev.breakin.infra",
        "dev.breakin.jdbc", // JDBC 구현체 클래스 스캔
        "dev.breakin.outbox",
        "dev.breakin.elasticsearch",
        "dev.breakin.sync",
        "dev.breakin.crawler"
})
@EnableJdbcRepositories(basePackages = "dev.breakin.jdbc")
public class ScanConfig {
}
