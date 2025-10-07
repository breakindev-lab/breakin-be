package dev.breakin.comment;

import dev.breakin.common.CommentOrder;
import lombok.Value;
import java.time.Instant;

@Value
public class Comment implements CommentModel {
    Long commentId;
    Long userId;
    String content;
    String targetType;
    Long targetId;
    Long parentId;
    CommentOrder commentOrder;
    Boolean isHidden;
    Instant createdAt;
    Instant updatedAt;

    public static Comment newComment(
        Long userId,
        String content,
        String targetType,
        Long targetId
    ) {
        Instant now = Instant.now();
        return new Comment(
            null,
            userId,
            content,
            targetType,
            targetId,
            null,
            CommentOrder.empty(),
            false,
            now,
            now
        );
    }

    public static Comment newReply(
        Long userId,
        String content,
        String targetType,
        Long targetId,
        Long parentId
    ) {
        Instant now = Instant.now();
        return new Comment(
            null,
            userId,
            content,
            targetType,
            targetId,
            parentId,
            CommentOrder.empty(),
            false,
            now,
            now
        );
    }

    public Comment hide() {
        return new Comment(
            commentId, userId, content, targetType, targetId,
            parentId, commentOrder, true, createdAt, Instant.now()
        );
    }

    public Comment show() {
        return new Comment(
            commentId, userId, content, targetType, targetId,
            parentId, commentOrder, false, createdAt, Instant.now()
        );
    }
}
