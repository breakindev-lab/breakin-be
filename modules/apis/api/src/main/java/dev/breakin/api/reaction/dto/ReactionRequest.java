package dev.breakin.api.reaction.dto;

import dev.breakin.model.common.TargetType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReactionRequest {
    @NotNull
    private Long userId;

    @NotNull
    private TargetType targetType;

    @NotNull
    private Long targetId;
}
