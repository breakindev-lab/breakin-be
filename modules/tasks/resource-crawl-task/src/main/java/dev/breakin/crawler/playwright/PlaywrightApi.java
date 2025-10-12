package dev.breakin.crawler.playwright;

public interface PlaywrightApi {
    String waitAndGetHtml(String url, int waitInSeconds) throws InterruptedException;
}
