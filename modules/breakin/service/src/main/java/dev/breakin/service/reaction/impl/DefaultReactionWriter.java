package dev.breakin.service.reaction.impl;

import dev.breakin.infra.reaction.repository.ReactionRepository;
import dev.breakin.model.common.TargetType;
import dev.breakin.model.reaction.Reaction;
import dev.breakin.model.reaction.ReactionIdentity;
import dev.breakin.model.reaction.ReactionType;
import dev.breakin.service.reaction.ReactionReader;
import dev.breakin.service.reaction.ReactionWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Reaction 서비스 구현체
 *
 * 좋아요/싫어요 반응을 처리하는 비즈니스 로직을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultReactionWriter implements ReactionWriter {

    private final ReactionReader reactionReader;
    private final ReactionRepository reactionRepository;

    @Override
    public void likeUp(Long userId, TargetType targetType, Long targetId) {
        log.info("User {} adding LIKE to {} {}", userId, targetType, targetId);

        Optional<Reaction> existing = reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);

        if (existing.isPresent()) {
            Reaction reaction = existing.get();
            if (reaction.getReactionType() == ReactionType.LIKE) {
                log.info("User {} already liked {} {}", userId, targetType, targetId);
                return;
            }
            // DISLIKE → LIKE로 변경
            Reaction updated = reaction.toggleReaction();
            reactionRepository.save(updated);
            log.info("User {} changed reaction from DISLIKE to LIKE on {} {}", userId, targetType, targetId);
        } else {
            // 새로운 LIKE 생성
            Reaction newReaction = Reaction.create(userId, targetType, targetId, ReactionType.LIKE);
            reactionRepository.save(newReaction);
            log.info("User {} created new LIKE on {} {}", userId, targetType, targetId);
        }
    }

    @Override
    public void dislikeUp(Long userId, TargetType targetType, Long targetId) {
        log.info("User {} adding DISLIKE to {} {}", userId, targetType, targetId);

        Optional<Reaction> existing = reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);

        if (existing.isPresent()) {
            Reaction reaction = existing.get();
            if (reaction.getReactionType() == ReactionType.DISLIKE) {
                log.info("User {} already disliked {} {}", userId, targetType, targetId);
                return;
            }
            // LIKE → DISLIKE로 변경
            Reaction updated = reaction.toggleReaction();
            reactionRepository.save(updated);
            log.info("User {} changed reaction from LIKE to DISLIKE on {} {}", userId, targetType, targetId);
        } else {
            // 새로운 DISLIKE 생성
            Reaction newReaction = Reaction.create(userId, targetType, targetId, ReactionType.DISLIKE);
            reactionRepository.save(newReaction);
            log.info("User {} created new DISLIKE on {} {}", userId, targetType, targetId);
        }
    }

    @Override
    public void likeDown(Long userId, TargetType targetType, Long targetId) {
        log.info("User {} removing LIKE from {} {}", userId, targetType, targetId);

        Optional<Reaction> existing = reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);

        if (existing.isEmpty()) {
            log.info("No reaction found for user {} on {} {}", userId, targetType, targetId);
            return;
        }

        Reaction reaction = existing.get();
        if (reaction.getReactionType() != ReactionType.LIKE) {
            log.warn("User {} tried to remove LIKE but has DISLIKE on {} {}", userId, targetType, targetId);
            return;
        }

        reactionRepository.deleteById(new ReactionIdentity(reaction.getReactionId()));
        log.info("User {} removed LIKE from {} {}", userId, targetType, targetId);
    }

    @Override
    public void dislikeDown(Long userId, TargetType targetType, Long targetId) {
        log.info("User {} removing DISLIKE from {} {}", userId, targetType, targetId);

        Optional<Reaction> existing = reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);

        if (existing.isEmpty()) {
            log.info("No reaction found for user {} on {} {}", userId, targetType, targetId);
            return;
        }

        Reaction reaction = existing.get();
        if (reaction.getReactionType() != ReactionType.DISLIKE) {
            log.warn("User {} tried to remove DISLIKE but has LIKE on {} {}", userId, targetType, targetId);
            return;
        }

        reactionRepository.deleteById(new ReactionIdentity(reaction.getReactionId()));
        log.info("User {} removed DISLIKE from {} {}", userId, targetType, targetId);
    }
}
