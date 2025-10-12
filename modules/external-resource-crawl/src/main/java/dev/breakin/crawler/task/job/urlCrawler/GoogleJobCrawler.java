package dev.breakin.crawler.batch.job.urlCrawler;

import dev.breakin.crawler.batch.job.urlCrawler.parser.GoogleUrlListParser;
import dev.breakin.crawler.playwright.PlaywrightApi;
import dev.breakin.crawler.step.CrawlJobUrlRepository;
import dev.breakin.model.common.Company;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleJobCrawler extends AbstractJobUrlCrawler {

    @Value("${crawler.job.listurl.google}")
    private String baseUrl;

    private final GoogleUrlListParser parser;

    public GoogleJobCrawler(PlaywrightApi playwrightApi,
                            CrawlJobUrlRepository urlRepository,
                            GoogleUrlListParser parser) {
        super(playwrightApi, urlRepository);
        this.parser = parser;
    }

    @Override
    protected String getBaseUrl() {
        return baseUrl;
    }

    @Override
    protected Company getCompany() {
        return Company.GOOGLE;
    }

    @Override
    protected UrlListParser getParser() {
        return parser;
    }
}
