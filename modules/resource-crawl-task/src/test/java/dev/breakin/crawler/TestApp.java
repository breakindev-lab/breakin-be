package dev.breakin.crawler;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "dev.breakin.infra",
        "dev.breakin.jdbc" , // JDBC 구현체 클래스 스캔
        "dev.breakin.crawler"
})
@EnableJdbcRepositories(basePackages = {"dev.breakin.jdbc", "dev.breakin.crawler"})
public class TestApp {
}
