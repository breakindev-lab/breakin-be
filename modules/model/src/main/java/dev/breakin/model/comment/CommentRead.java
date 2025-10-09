package dev.breakin.model.comment;

import dev.breakin.model.AuditProps;
import dev.breakin.model.common.CommentOrder;
import dev.breakin.model.common.TargetType;
import lombok.Value;
import java.time.Instant;

/**
 * CommentRead - Query model for reading comments with user information
 *
 * This model includes user nickname for API responses.
 * Use Comment model for write operations (create, update).
 */
@Value
public class CommentRead implements AuditProps {
    Long commentId;
    Long userId;
    String nickname;  // User nickname - fetched via JOIN
    String content;
    TargetType targetType;
    Long targetId;
    Long parentId;
    CommentOrder commentOrder;
    Boolean isHidden;
    Instant createdAt;
    Instant updatedAt;
}
