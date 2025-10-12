package dev.breakin.crawler.firecrawl;

public class FirecrawlException extends RuntimeException {

    public FirecrawlException(String message) {
        super(message);
    }

    public FirecrawlException(String message, Throwable cause) {
        super(message, cause);
    }
}
