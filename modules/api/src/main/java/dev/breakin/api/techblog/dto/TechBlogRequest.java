package dev.breakin.api.techblog.dto;

import dev.breakin.model.common.Popularity;
import dev.breakin.model.techblog.TechBlog;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@NoArgsConstructor
public class TechBlogRequest {
    private Long techBlogId;

    @NotBlank
    private String url;

    private String company;

    @NotBlank
    private String title;

    @NotBlank
    private String markdownBody;

    private String thumbnailUrl;
    private List<String> tags;
    private List<String> techCategories;
    private String originalUrl;

    public TechBlog toTechBlog() {
        Instant now = Instant.now();
        return new TechBlog(
                techBlogId,
                url,
                company,
                title,
                markdownBody,
                thumbnailUrl,
                tags != null ? tags : List.of(),
                techCategories != null ? techCategories : List.of(),
                originalUrl,
                Popularity.empty(),
                false,
                now,
                now
        );
    }
}
