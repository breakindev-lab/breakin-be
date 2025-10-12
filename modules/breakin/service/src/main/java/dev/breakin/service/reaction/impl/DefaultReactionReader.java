package dev.breakin.service.reaction.impl;

import dev.breakin.exception.reaction.ReactionNotFoundException;
import dev.breakin.model.common.TargetType;
import dev.breakin.model.reaction.Reaction;
import dev.breakin.model.reaction.ReactionIdentity;
import dev.breakin.service.reaction.ReactionReader;
import dev.breakin.infra.reaction.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Reaction 도메인 조회 서비스 구현체
 *
 * CQRS 패턴의 Query 책임을 구현하며,
 * Infrastructure Repository를 활용한 조회 로직을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultReactionReader implements ReactionReader {

    private final ReactionRepository reactionRepository;

    @Override
    public Reaction getById(ReactionIdentity identity) {
        log.debug("Fetching Reaction by id: {}", identity.getReactionId());
        return reactionRepository.findById(identity)
                .orElseThrow(() -> new ReactionNotFoundException("id: " + identity.getReactionId()));
    }

    @Override
    public List<Reaction> getAll() {
        log.debug("Fetching all Reactions");
        return reactionRepository.findAll();
    }

    @Override
    public List<Reaction> getByUserId(Long userId) {
        log.debug("Fetching Reactions by userId: {}", userId);
        return reactionRepository.findByUserId(userId);
    }

    @Override
    public List<Reaction> getByTargetTypeAndTargetId(TargetType targetType, Long targetId) {
        log.debug("Fetching Reactions by targetType: {} and targetId: {}", targetType, targetId);
        return reactionRepository.findByTargetTypeAndTargetId(targetType, targetId);
    }

    @Override
    public Optional<Reaction> findByUserIdAndTargetTypeAndTargetId(Long userId, TargetType targetType, Long targetId) {
        log.debug("Fetching Reaction by userId: {}, targetType: {}, targetId: {}", userId, targetType, targetId);
        return reactionRepository.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
    }
}
