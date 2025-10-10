package dev.breakin.crawler.firecrawl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FirecrawlData(
        String markdown,
        FirecrawlMetadata metadata
) {
}
