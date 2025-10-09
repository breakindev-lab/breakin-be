package dev.breakin.service.reaction.impl;

import dev.breakin.infra.reaction.repository.ReactionRepository;
import dev.breakin.model.common.TargetType;
import dev.breakin.model.reaction.Reaction;
import dev.breakin.model.reaction.ReactionIdentity;
import dev.breakin.model.reaction.ReactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultReactionReaderTest {

    @Mock
    private ReactionRepository reactionRepository;

    @InjectMocks
    private DefaultReactionReader reactionReader;

    private final Reaction sampleReaction = new Reaction(
        1L,                  // reactionId
        1L,                  // userId
        TargetType.JOB,      // targetType
        100L,                // targetId
        ReactionType.LIKE,   // reactionType
        Instant.now(),       // createdAt
        Instant.now()        // updatedAt
    );

    private final ReactionIdentity testIdentity = new ReactionIdentity(1L);

    @Test
    void getById_existingId_returnsReaction() {
        // given
        when(reactionRepository.findById(testIdentity))
            .thenReturn(Optional.of(sampleReaction));

        // when
        Reaction result = reactionReader.getById(testIdentity);

        // then
        assertNotNull(result);
        assertEquals(sampleReaction.getReactionId(), result.getReactionId());
        assertEquals(sampleReaction.getReactionType(), result.getReactionType());
        verify(reactionRepository).findById(testIdentity);
    }

    @Test
    void getById_nonExistingId_throwsException() {
        // given
        when(reactionRepository.findById(testIdentity))
            .thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () ->
            reactionReader.getById(testIdentity)
        );
        verify(reactionRepository).findById(testIdentity);
    }

    @Test
    void getAll_withData_returnsList() {
        // given
        List<Reaction> reactions = List.of(sampleReaction);
        when(reactionRepository.findAll())
            .thenReturn(reactions);

        // when
        List<Reaction> result = reactionReader.getAll();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleReaction.getReactionId(), result.get(0).getReactionId());
        verify(reactionRepository).findAll();
    }

    @Test
    void getAll_emptyData_returnsEmptyList() {
        // given
        when(reactionRepository.findAll())
            .thenReturn(List.of());

        // when
        List<Reaction> result = reactionReader.getAll();

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(reactionRepository).findAll();
    }

    @Test
    void getByUserId_existingUser_returnsList() {
        // given
        Long userId = 1L;
        List<Reaction> reactions = List.of(sampleReaction);
        when(reactionRepository.findByUserId(userId))
            .thenReturn(reactions);

        // when
        List<Reaction> result = reactionReader.getByUserId(userId);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUserId());
        verify(reactionRepository).findByUserId(userId);
    }

    @Test
    void getByUserId_nonExistingUser_returnsEmptyList() {
        // given
        Long userId = 999L;
        when(reactionRepository.findByUserId(userId))
            .thenReturn(List.of());

        // when
        List<Reaction> result = reactionReader.getByUserId(userId);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(reactionRepository).findByUserId(userId);
    }

    @Test
    void getByTargetTypeAndTargetId_existingTarget_returnsList() {
        // given
        TargetType targetType = TargetType.JOB;
        Long targetId = 100L;
        List<Reaction> reactions = List.of(sampleReaction);
        when(reactionRepository.findByTargetTypeAndTargetId(targetType, targetId))
            .thenReturn(reactions);

        // when
        List<Reaction> result = reactionReader.getByTargetTypeAndTargetId(targetType, targetId);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(targetType, result.get(0).getTargetType());
        assertEquals(targetId, result.get(0).getTargetId());
        verify(reactionRepository).findByTargetTypeAndTargetId(targetType, targetId);
    }

    @Test
    void getByTargetTypeAndTargetId_nonExistingTarget_returnsEmptyList() {
        // given
        TargetType targetType = TargetType.JOB;
        Long targetId = 999L;
        when(reactionRepository.findByTargetTypeAndTargetId(targetType, targetId))
            .thenReturn(List.of());

        // when
        List<Reaction> result = reactionReader.getByTargetTypeAndTargetId(targetType, targetId);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(reactionRepository).findByTargetTypeAndTargetId(targetType, targetId);
    }

    @Test
    void findByUserIdAndTargetTypeAndTargetId_existingReaction_returnsOptionalWithReaction() {
        // given
        Long userId = 1L;
        TargetType targetType = TargetType.JOB;
        Long targetId = 100L;
        when(reactionRepository.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId))
            .thenReturn(Optional.of(sampleReaction));

        // when
        Optional<Reaction> result = reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);

        // then
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getUserId());
        assertEquals(targetType, result.get().getTargetType());
        assertEquals(targetId, result.get().getTargetId());
        verify(reactionRepository).findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
    }

    @Test
    void findByUserIdAndTargetTypeAndTargetId_nonExistingReaction_returnsEmptyOptional() {
        // given
        Long userId = 999L;
        TargetType targetType = TargetType.JOB;
        Long targetId = 100L;
        when(reactionRepository.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId))
            .thenReturn(Optional.empty());

        // when
        Optional<Reaction> result = reactionReader.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);

        // then
        assertFalse(result.isPresent());
        verify(reactionRepository).findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
    }
}
