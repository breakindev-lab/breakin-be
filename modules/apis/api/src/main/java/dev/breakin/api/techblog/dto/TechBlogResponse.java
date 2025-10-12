package dev.breakin.api.techblog.dto;

import dev.breakin.model.common.Popularity;
import dev.breakin.model.techblog.TechBlog;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
public class TechBlogResponse {
    private final Long techBlogId;
    private final String url;
    private final String company;
    private final String title;
    private final String markdownBody;
    private final String thumbnailUrl;
    private final List<String> tags;
    private final List<String> techCategories;
    private final String originalUrl;
    private final Popularity popularity;
    private final Boolean isDeleted;
    private final Instant createdAt;
    private final Instant updatedAt;

    public static TechBlogResponse from(TechBlog techBlog) {
        return new TechBlogResponse(
                techBlog.getTechBlogId(),
                techBlog.getUrl(),
                techBlog.getCompany(),
                techBlog.getTitle(),
                techBlog.getMarkdownBody(),
                techBlog.getThumbnailUrl(),
                techBlog.getTags(),
                techBlog.getTechCategories(),
                techBlog.getOriginalUrl(),
                techBlog.getPopularity(),
                techBlog.getIsDeleted(),
                techBlog.getCreatedAt(),
                techBlog.getUpdatedAt()
        );
    }
}
