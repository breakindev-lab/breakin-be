package dev.breakin.service.reaction.impl;

import dev.breakin.infra.reaction.repository.ReactionRepository;
import dev.breakin.model.common.TargetType;
import dev.breakin.model.reaction.Reaction;
import dev.breakin.model.reaction.ReactionIdentity;
import dev.breakin.model.reaction.ReactionType;
import dev.breakin.service.reaction.ReactionReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultReactionWriterTest {

    @Mock
    private ReactionReader reactionReader;

    @Mock
    private ReactionRepository reactionRepository;

    @InjectMocks
    private DefaultReactionWriter reactionWriter;

    private final Long userId = 1L;
    private final TargetType targetType = TargetType.JOB;
    private final Long targetId = 100L;

    private final Reaction likeReaction = new Reaction(
        1L, userId, targetType, targetId, ReactionType.LIKE,
        Instant.now(), Instant.now()
    );

    private final Reaction dislikeReaction = new Reaction(
        1L, userId, targetType, targetId, ReactionType.DISLIKE,
        Instant.now(), Instant.now()
    );

    @Test
    void likeUp_noExistingReaction_createsNewLike() {
        // given
        when(reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId))
            .thenReturn(Optional.empty());
        when(reactionRepository.save(any(Reaction.class)))
            .thenReturn(likeReaction);

        // when
        reactionWriter.likeUp(userId, targetType, targetId);

        // then
        verify(reactionReader).findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
        verify(reactionRepository).save(any(Reaction.class));
    }

    @Test
    void likeUp_existingLike_doesNothing() {
        // given
        when(reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId))
            .thenReturn(Optional.of(likeReaction));

        // when
        reactionWriter.likeUp(userId, targetType, targetId);

        // then
        verify(reactionReader).findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
        verify(reactionRepository, never()).save(any(Reaction.class));
    }

    @Test
    void likeUp_existingDislike_togglesToLike() {
        // given
        when(reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId))
            .thenReturn(Optional.of(dislikeReaction));
        when(reactionRepository.save(any(Reaction.class)))
            .thenReturn(likeReaction);

        // when
        reactionWriter.likeUp(userId, targetType, targetId);

        // then
        verify(reactionReader).findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
        verify(reactionRepository).save(any(Reaction.class));
    }

    @Test
    void dislikeUp_noExistingReaction_createsNewDislike() {
        // given
        when(reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId))
            .thenReturn(Optional.empty());
        when(reactionRepository.save(any(Reaction.class)))
            .thenReturn(dislikeReaction);

        // when
        reactionWriter.dislikeUp(userId, targetType, targetId);

        // then
        verify(reactionReader).findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
        verify(reactionRepository).save(any(Reaction.class));
    }

    @Test
    void dislikeUp_existingDislike_doesNothing() {
        // given
        when(reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId))
            .thenReturn(Optional.of(dislikeReaction));

        // when
        reactionWriter.dislikeUp(userId, targetType, targetId);

        // then
        verify(reactionReader).findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
        verify(reactionRepository, never()).save(any(Reaction.class));
    }

    @Test
    void dislikeUp_existingLike_togglesToDislike() {
        // given
        when(reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId))
            .thenReturn(Optional.of(likeReaction));
        when(reactionRepository.save(any(Reaction.class)))
            .thenReturn(dislikeReaction);

        // when
        reactionWriter.dislikeUp(userId, targetType, targetId);

        // then
        verify(reactionReader).findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
        verify(reactionRepository).save(any(Reaction.class));
    }

    @Test
    void likeDown_existingLike_deletesReaction() {
        // given
        when(reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId))
            .thenReturn(Optional.of(likeReaction));
        doNothing().when(reactionRepository).deleteById(any(ReactionIdentity.class));

        // when
        reactionWriter.likeDown(userId, targetType, targetId);

        // then
        verify(reactionReader).findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
        verify(reactionRepository).deleteById(new ReactionIdentity(likeReaction.getReactionId()));
    }

    @Test
    void likeDown_noExistingReaction_doesNothing() {
        // given
        when(reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId))
            .thenReturn(Optional.empty());

        // when
        reactionWriter.likeDown(userId, targetType, targetId);

        // then
        verify(reactionReader).findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
        verify(reactionRepository, never()).deleteById(any(ReactionIdentity.class));
    }

    @Test
    void likeDown_existingDislike_doesNothing() {
        // given
        when(reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId))
            .thenReturn(Optional.of(dislikeReaction));

        // when
        reactionWriter.likeDown(userId, targetType, targetId);

        // then
        verify(reactionReader).findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
        verify(reactionRepository, never()).deleteById(any(ReactionIdentity.class));
    }

    @Test
    void dislikeDown_existingDislike_deletesReaction() {
        // given
        when(reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId))
            .thenReturn(Optional.of(dislikeReaction));
        doNothing().when(reactionRepository).deleteById(any(ReactionIdentity.class));

        // when
        reactionWriter.dislikeDown(userId, targetType, targetId);

        // then
        verify(reactionReader).findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
        verify(reactionRepository).deleteById(new ReactionIdentity(dislikeReaction.getReactionId()));
    }

    @Test
    void dislikeDown_noExistingReaction_doesNothing() {
        // given
        when(reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId))
            .thenReturn(Optional.empty());

        // when
        reactionWriter.dislikeDown(userId, targetType, targetId);

        // then
        verify(reactionReader).findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
        verify(reactionRepository, never()).deleteById(any(ReactionIdentity.class));
    }

    @Test
    void dislikeDown_existingLike_doesNothing() {
        // given
        when(reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId))
            .thenReturn(Optional.of(likeReaction));

        // when
        reactionWriter.dislikeDown(userId, targetType, targetId);

        // then
        verify(reactionReader).findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
        verify(reactionRepository, never()).deleteById(any(ReactionIdentity.class));
    }
}
