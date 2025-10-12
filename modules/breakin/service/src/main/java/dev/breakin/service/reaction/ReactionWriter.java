package dev.breakin.service.reaction;

import dev.breakin.model.common.TargetType;

/**
 * Reaction 서비스 인터페이스
 *
 * 좋아요/싫어요 반응을 처리하는 비즈니스 로직을 제공합니다.
 */
public interface ReactionWriter {

    /**
     * 좋아요 추가
     *
     * @param userId 사용자 ID
     * @param targetType 대상 타입
     * @param targetId 대상 ID
     */
    void likeUp(Long userId, TargetType targetType, Long targetId);

    /**
     * 싫어요 추가
     *
     * @param userId 사용자 ID
     * @param targetType 대상 타입
     * @param targetId 대상 ID
     */
    void dislikeUp(Long userId, TargetType targetType, Long targetId);

    /**
     * 좋아요 취소
     *
     * @param userId 사용자 ID
     * @param targetType 대상 타입
     * @param targetId 대상 ID
     */
    void likeDown(Long userId, TargetType targetType, Long targetId);

    /**
     * 싫어요 취소
     *
     * @param userId 사용자 ID
     * @param targetType 대상 타입
     * @param targetId 대상 ID
     */
    void dislikeDown(Long userId, TargetType targetType, Long targetId);
}
