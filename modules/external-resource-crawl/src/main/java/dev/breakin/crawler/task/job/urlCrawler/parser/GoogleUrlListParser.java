package dev.breakin.crawler.batch.job.urlCrawler.parser;

import dev.breakin.crawler.batch.job.urlCrawler.JobUrlInfo;
import dev.breakin.crawler.batch.job.urlCrawler.UrlListParser;
import dev.breakin.crawler.jsoup.JsoupWalker;
import dev.breakin.crawler.urlUtils.UrlCleaner;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class GoogleUrlListParser implements UrlListParser {

    @Override
    public List<JobUrlInfo> extractUrls(String html) {
        List<JobUrlInfo> urls = new ArrayList<>();

        Document doc = JsoupWalker.makeDoc(html);

        // ---------- PATH TO <ul> ----------
        // 1) body(1)
        Element body1 = JsoupWalker.nthBody(doc, 1);

        // 2) c-wiz(2) under body
        Element cwiz2 = JsoupWalker.nth(body1, "c-wiz", 2);

        // 3) div(1)
        Element div_a1 = JsoupWalker.nthDiv(cwiz2, 1);

        // 4) div(2)
        Element div_a2 = JsoupWalker.nthDiv(div_a1, 2);

        // 5) div(1)
        Element div_a2_1 = JsoupWalker.nthDiv(div_a2, 1);

        // 6) div(1)
        Element div_a2_1_1 = JsoupWalker.nthDiv(div_a2_1, 1);

        // 7) div(2)
        Element div_a2_1_1_2 = JsoupWalker.nthDiv(div_a2_1_1, 2);

        // 8) main(1)
        Element main1 = JsoupWalker.nth(div_a2_1_1_2, "main", 1);

        // 9) div(1)
        Element main1_div1 = JsoupWalker.nthDiv(main1, 1);

        // 10) c-wiz(1)
        Element cwiz_b1 = JsoupWalker.nth(main1_div1, "c-wiz", 1);

        // 11) div(1)
        Element cwiz_b1_div1 = JsoupWalker.nthDiv(cwiz_b1, 1);

        // 12) ul(1)  ← target UL
        Element targetUl = JsoupWalker.nth(cwiz_b1_div1, "ul", 1);

        if (targetUl == null) {
            log.error("Target <ul> not found.");
            return List.of();
        }

        // ---------- ITERATE li (direct children only) ----------
        for (Element li : targetUl.children()) {
            if (!"li".equalsIgnoreCase(li.tagName())) continue;

            // li-path:
            // 1) div(1)
            Element li_div1 = JsoupWalker.nthDiv(li, 1);

            // 2) div(1)
            Element li_div1_1 = JsoupWalker.nthDiv(li_div1, 1);

            // 3) div(1)
            Element li_div1_1_1 = JsoupWalker.nthDiv(li_div1_1, 1);

            // 4) div(1)
            Element li_div1_1_1_1 = JsoupWalker.nthDiv(li_div1_1_1, 1);

            var title = extractTitle(li_div1_1_1_1);
            // 5) div(5)
            Element li_div1_1_1_1_5 = JsoupWalker.nthDiv(li_div1_1_1_1, 5);

            // 6) div(1)
            Element li_div1_1_1_1_5_1 = JsoupWalker.nthDiv(li_div1_1_1_1_5, 1);

            // 7) div(1)
            Element li_div1_1_1_1_5_1_1 = JsoupWalker.nthSpan(li_div1_1_1_1_5_1, 1);

            Element anchor = JsoupWalker.nth(li_div1_1_1_1_5_1, "a", 1);

            // extract href
            String href = JsoupWalker.absHrefOrEmpty(anchor);  // absolute href if baseUri was set
            if (href.isEmpty() && anchor != null) {
                href = anchor.attr("href").trim();            // fallback to raw (relative) href
            }

            // (optional) also capture label text if you need it
            String label = JsoupWalker.textOrEmpty(
                    JsoupWalker.nth(li_div1_1_1_1_5_1, "span", 1)     // first direct-child <span>
            );

            // debug print
            href = UrlCleaner.stripAllQuery(href);
            urls.add(new JobUrlInfo(href, title));

        }
        return urls;
    }

    private String extractTitle(Element liDiv1111) {

        Element container = JsoupWalker.nthDiv(liDiv1111, 1);

// 1) inner div(1)
        Element innerDiv = JsoupWalker.requireNth(container, "div", 1);

// 2) h3(1) under innerDiv
        Element h3 = JsoupWalker.requireNth(innerDiv, "h3", 1); // or: JsoupWalker.nthH(innerDiv, 3, 1)

// 3) title text
        String title = JsoupWalker.textOrEmpty(h3);
        return title;
    }
}
