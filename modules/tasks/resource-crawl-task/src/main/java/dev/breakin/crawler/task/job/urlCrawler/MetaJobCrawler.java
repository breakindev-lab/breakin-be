package dev.breakin.crawler.task.job.urlCrawler;

import dev.breakin.crawler.task.job.urlCrawler.parser.MetaUrlListParser;
import dev.breakin.crawler.playwright.PlaywrightApi;
import dev.breakin.crawler.step.CrawlJobUrlRepository;
import dev.breakin.model.common.Company;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MetaJobCrawler extends AbstractJobUrlCrawler {

    @Value("${crawler.job.listurl.meta}")
    private String baseUrl;

    private final MetaUrlListParser parser;

    public MetaJobCrawler(PlaywrightApi playwrightApi,
                            CrawlJobUrlRepository urlRepository,
                            MetaUrlListParser parser) {
        super(playwrightApi, urlRepository);
        this.parser = parser;
    }

    @Override
    protected String getBaseUrl() {
        return baseUrl;
    }

    @Override
    protected Company getCompany() {
        return Company.META;
    }

    @Override
    protected UrlListParser getParser() {
        return parser;
    }
}
