package dev.breakin.model.reaction;

import dev.breakin.model.AuditProps;
import dev.breakin.model.common.TargetType;
import lombok.Value;
import java.time.Instant;

@Value
public class Reaction implements AuditProps {
    Long reactionId;
    Long userId;
    TargetType targetType;
    Long targetId;
    ReactionType reactionType;
    Instant createdAt;
    Instant updatedAt;

    public static Reaction create(
        Long userId,
        TargetType targetType,
        Long targetId,
        ReactionType reactionType
    ) {
        Instant now = Instant.now();
        return new Reaction(
            null,
            userId,
            targetType,
            targetId,
            reactionType,
            now,
            now
        );
    }

    public Reaction toggleReaction() {
        ReactionType newType = (reactionType == ReactionType.LIKE)
            ? ReactionType.DISLIKE
            : ReactionType.LIKE;
        return new Reaction(
            reactionId,
            userId,
            targetType,
            targetId,
            newType,
            createdAt,
            Instant.now()
        );
    }
}
