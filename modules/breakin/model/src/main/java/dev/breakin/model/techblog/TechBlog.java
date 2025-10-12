package dev.breakin.model.techblog;

import dev.breakin.model.AuditProps;
import dev.breakin.model.common.Popularity;
import lombok.Value;
import java.time.Instant;
import java.util.List;

@Value
public class TechBlog implements AuditProps {
    Long techBlogId;
    String url;
    String company;
    String title;
    String markdownBody;
    String thumbnailUrl;
    List<String> tags;
    List<String> techCategories;
    String originalUrl;
    Popularity popularity;
    Boolean isDeleted;
    Instant createdAt;
    Instant updatedAt;

    public static TechBlog newBlog(
        String url,
        String title,
        String markdownBody
    ) {
        Instant now = Instant.now();
        return new TechBlog(
            null,
            url,
            null,
            title,
            markdownBody,
            null,
            List.of(),
            List.of(),
            null,
            Popularity.empty(),
            false,
            now,
            now
        );
    }

    public static TechBlog newExternalBlog(
        String url,
        String company,
        String title,
        String markdownBody,
        String originalUrl
    ) {
        Instant now = Instant.now();
        return new TechBlog(
            null,
            url,
            company,
            title,
            markdownBody,
            null,
            List.of(),
            List.of(),
            originalUrl,
            Popularity.empty(),
            false,
            now,
            now
        );
    }

    public TechBlog incrementViewCount() {
        return new TechBlog(
            techBlogId, url, company, title, markdownBody, thumbnailUrl,
            tags, techCategories, originalUrl, popularity.incrementViewCount(),
            isDeleted, createdAt, Instant.now()
        );
    }

    public TechBlog incrementViewCount(long adder) {
        return new TechBlog(
                techBlogId, url, company, title, markdownBody, thumbnailUrl,
                tags, techCategories, originalUrl, popularity.incrementViewCount(adder),
                isDeleted, createdAt, Instant.now()
        );
    }

    public TechBlog incrementCommentCount() {
        return new TechBlog(
            techBlogId, url, company, title, markdownBody, thumbnailUrl,
            tags, techCategories, originalUrl, popularity.incrementCommentCount(),
            isDeleted, createdAt, Instant.now()
        );
    }

    public TechBlog incrementLikeCount() {
        return new TechBlog(
            techBlogId, url, company, title, markdownBody, thumbnailUrl,
            tags, techCategories, originalUrl, popularity.incrementLikeCount(),
            isDeleted, createdAt, Instant.now()
        );
    }

    public TechBlog decrementLikeCount() {
        return new TechBlog(
            techBlogId, url, company, title, markdownBody, thumbnailUrl,
            tags, techCategories, originalUrl, popularity.decrementLikeCount(),
            isDeleted, createdAt, Instant.now()
        );
    }

    public TechBlog markAsDeleted() {
        return new TechBlog(
            techBlogId, url, company, title, markdownBody, thumbnailUrl,
            tags, techCategories, originalUrl, popularity, true,
            createdAt, Instant.now()
        );
    }
}
