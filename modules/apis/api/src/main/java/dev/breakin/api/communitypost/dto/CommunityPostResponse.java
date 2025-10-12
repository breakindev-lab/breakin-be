package dev.breakin.api.communitypost.dto;

import dev.breakin.model.common.Popularity;
import dev.breakin.model.communitypost.CommunityPostRead;
import dev.breakin.model.communitypost.CommunityPostCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@Getter
@AllArgsConstructor
@Schema(description = "Community post response")
public class CommunityPostResponse {
    @Schema(description = "Community post ID", example = "1")
    private final Long communityPostId;

    @Schema(description = "User ID", example = "1")
    private final Long userId;

    @Schema(description = "User nickname", example = "testuser")
    private final String nickname;

    @Schema(description = "Category", example = "INTERVIEW_SHARE")
    private final CommunityPostCategory category;

    @Schema(description = "Title", example = "My interview experience")
    private final String title;

    @Schema(description = "Markdown body")
    private final String markdownBody;

    @Schema(description = "Company", example = "Company A")
    private final String company;

    @Schema(description = "Location", example = "Seoul")
    private final String location;

    @Schema(description = "Linked job ID", example = "100")
    private final Long linkedJobId;

    @Schema(description = "Is from job comment", example = "false")
    private final Boolean isFromJobComment;

    @Schema(description = "Popularity")
    private final Popularity popularity;

    @Schema(description = "Is deleted", example = "false")
    private final Boolean isDeleted;

    @Schema(description = "Created at", example = "2025-10-09T14:30:00Z")
    private final Instant createdAt;

    @Schema(description = "Updated at", example = "2025-10-09T14:30:00Z")
    private final Instant updatedAt;

    public static CommunityPostResponse from(CommunityPostRead communityPostRead) {
        return new CommunityPostResponse(
                communityPostRead.getCommunityPostId(),
                communityPostRead.getUserId(),
                communityPostRead.getNickname(),
                communityPostRead.getCategory(),
                communityPostRead.getTitle(),
                communityPostRead.getMarkdownBody(),
                communityPostRead.getCompany(),
                communityPostRead.getLocation(),
                communityPostRead.getLinkedJobId(),
                communityPostRead.getIsFromJobComment(),
                communityPostRead.getPopularity(),
                communityPostRead.getIsDeleted(),
                communityPostRead.getCreatedAt(),
                communityPostRead.getUpdatedAt()
        );
    }
}
