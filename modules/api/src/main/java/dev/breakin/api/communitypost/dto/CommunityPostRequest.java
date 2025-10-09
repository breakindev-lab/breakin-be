package dev.breakin.api.communitypost.dto;

import dev.breakin.model.common.Popularity;
import dev.breakin.model.communitypost.CommunityPost;
import dev.breakin.model.communitypost.CommunityPostCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
public class CommunityPostRequest {
    private Long communityPostId;

    @NotNull
    private Long userId;

    @NotNull
    private CommunityPostCategory category;

    @NotBlank
    private String title;

    @NotBlank
    private String markdownBody;

    private String company;
    private String location;
    private Long linkedJobId;
    private Boolean isFromJobComment;

    public CommunityPost toCommunityPost() {
        Instant now = Instant.now();
        return new CommunityPost(
                communityPostId,
                userId,
                category,
                title,
                markdownBody,
                company,
                location,
                linkedJobId,
                isFromJobComment != null ? isFromJobComment : false,
                Popularity.empty(),
                false,
                now,
                now
        );
    }
}
