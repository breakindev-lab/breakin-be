package dev.breakin.model.communitypost;

import dev.breakin.model.AuditProps;
import dev.breakin.model.common.Popularity;
import lombok.Value;
import java.time.Instant;

/**
 * CommunityPostRead - Query model for reading community posts with user information
 *
 * This model includes user nickname for API responses.
 * Use CommunityPost model for write operations (create, update).
 */
@Value
public class CommunityPostRead implements AuditProps {
    Long communityPostId;
    Long userId;
    String nickname;  // User nickname - fetched via JOIN
    CommunityPostCategory category;
    String title;
    String markdownBody;
    String company;
    String location;
    Long linkedJobId;
    Boolean isFromJobComment;
    Popularity popularity;
    Boolean isDeleted;
    Instant createdAt;
    Instant updatedAt;
}
