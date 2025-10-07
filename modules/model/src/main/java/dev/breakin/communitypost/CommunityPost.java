package dev.breakin.communitypost;

import dev.breakin.common.Popularity;
import lombok.Value;
import java.time.Instant;

@Value
public class CommunityPost implements CommunityPostModel {
    Long communityPostId;
    Long userId;
    String category;
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

    public static CommunityPost newPost(
        Long userId,
        String category,
        String title,
        String markdownBody
    ) {
        Instant now = Instant.now();
        return new CommunityPost(
            null,
            userId,
            category,
            title,
            markdownBody,
            null,
            null,
            null,
            false,
            Popularity.empty(),
            false,
            now,
            now
        );
    }

    public static CommunityPost fromJobComment(
        Long userId,
        String title,
        String markdownBody,
        Long linkedJobId,
        String company,
        String location
    ) {
        Instant now = Instant.now();
        return new CommunityPost(
            null,
            userId,
            "INTERVIEW_SHARE",
            title,
            markdownBody,
            company,
            location,
            linkedJobId,
            true,
            Popularity.empty(),
            false,
            now,
            now
        );
    }

    public CommunityPost incrementViewCount() {
        return new CommunityPost(
            communityPostId, userId, category, title, markdownBody,
            company, location, linkedJobId, isFromJobComment,
            popularity.incrementViewCount(), isDeleted, createdAt, Instant.now()
        );
    }

    public CommunityPost incrementCommentCount() {
        return new CommunityPost(
            communityPostId, userId, category, title, markdownBody,
            company, location, linkedJobId, isFromJobComment,
            popularity.incrementCommentCount(), isDeleted, createdAt, Instant.now()
        );
    }

    public CommunityPost incrementLikeCount() {
        return new CommunityPost(
            communityPostId, userId, category, title, markdownBody,
            company, location, linkedJobId, isFromJobComment,
            popularity.incrementLikeCount(), isDeleted, createdAt, Instant.now()
        );
    }

    public CommunityPost decrementLikeCount() {
        return new CommunityPost(
            communityPostId, userId, category, title, markdownBody,
            company, location, linkedJobId, isFromJobComment,
            popularity.decrementLikeCount(), isDeleted, createdAt, Instant.now()
        );
    }

    public CommunityPost markAsDeleted() {
        return new CommunityPost(
            communityPostId, userId, category, title, markdownBody,
            company, location, linkedJobId, isFromJobComment,
            popularity, true, createdAt, Instant.now()
        );
    }
}
