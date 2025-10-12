package dev.breakin.crawler.task.job.urlCrawler.parser;

import dev.breakin.crawler.task.job.urlCrawler.JobUrlInfo;
import dev.breakin.crawler.task.job.urlCrawler.UrlListParser;
import dev.breakin.crawler.jsoup.JsoupWalker;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class MetaUrlListParser implements UrlListParser {

    private static final String META_JOB_URL_BASE = "https://www.metacareers.com";

    @Override
    public List<JobUrlInfo> extractUrls(String html) {

        var doc = JsoupWalker.makeDoc(html);
        // 1) body(1)
        Element s1_body1 = JsoupWalker.requireBody(doc, 1);

        // 2) div(1)
        Element s2_div1 = JsoupWalker.requireNth(s1_body1, "div", 1);

        // 3) div(1)
        Element s3_div1 = JsoupWalker.requireNth(s2_div1, "div", 1);

        // 4) div(3)
        Element s4_div3 = JsoupWalker.requireNth(s3_div1, "div", 3);

        // 5) div(1)
        Element s5_div1 = JsoupWalker.requireNth(s4_div3, "div", 1);

        // 6) div(1)   // interpreted from "6.1th div"
        Element s6_div1 = JsoupWalker.requireNth(s5_div1, "div", 1);

        // 7) div(1)
        Element s7_div1 = JsoupWalker.requireNth(s6_div1, "div", 1);

        // 8) div(1)
        Element s8_div1 = JsoupWalker.requireNth(s7_div1, "div", 1);

        // 9) div(3)
        Element s9_div3 = JsoupWalker.requireNth(s8_div1, "div", 3);

        // 10) div(1)
        Element s10_div1 = JsoupWalker.requireNth(s9_div3, "div", 1);

        // 11) div(2)
        Element s11_div2 = JsoupWalker.requireNth(s10_div1, "div", 2);

        // 12) div(1)
        Element s12_div1 = JsoupWalker.requireNth(s11_div2, "div", 1);

        // 13) div(1)
        Element s13_div1 = JsoupWalker.requireNth(s12_div1, "div", 1);

        // 14) div(1)  ← final target at this path
        Element s14_div1 = JsoupWalker.requireNth(s13_div1, "div", 1);

        // Quick debug outputs (inspect variables in debugger as well)
        System.out.println("Final tag: " + s14_div1.tagName());
        System.out.println("Final snippet:");
        System.out.println(s14_div1.outerHtml());


        return listDetailUrls(s14_div1);
    }

    /**
     * Returns hrefs of direct child <a> elements (order preserved, duplicates removed).
     */
    public static List<JobUrlInfo> listDetailUrls(Element container) {
        if (container == null) return List.of();

        Elements directAnchors = container.select("> a[href]"); // ONLY direct children
        Set<JobUrlInfo> dedup = new LinkedHashSet<>();

        for (Element a : directAnchors) {
            String href = a.absUrl("href");       // absolute if baseUri was set on Document
            var title = extractTitleStrict(a);
            if (href.isEmpty()) href = a.attr("href"); // fallback to raw relative
            if (!href.isBlank()) dedup.add(new JobUrlInfo(META_JOB_URL_BASE + href, title));
        }
        return new ArrayList<>(dedup);
    }

    // A) Step-by-step (direct children only) — debugger-friendly
    public static String extractTitleStrict(Element container) {
        if (container == null) return "";

        // 1) a(1) under container
        //Element a1 = JsoupWalker.requireNth(container, "a", 1);

        // 2) div(1) under a
        Element a1_div1 = JsoupWalker.requireNth(container, "div", 1);

        // 3) div(1) under div
        Element a1_div1_1 = JsoupWalker.requireNth(a1_div1, "div", 1);

        // 4) div(1) under div  (this one holds aria-hidden block)
        Element a1_div1_1_1 = JsoupWalker.requireNth(a1_div1_1, "div", 1);

        // 5) div(1) under that block  (wrapper of <h3>)
        Element a1_div1_1_1_1 = JsoupWalker.requireNth(a1_div1_1_1, "div", 1);

        // 6) h3(1) — the title node
        Element h3 = JsoupWalker.requireNth(a1_div1_1_1_1, "h3", 1);

        return JsoupWalker.textOrEmpty(h3);
    }
}
