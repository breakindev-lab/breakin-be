package dev.breakin.service.comment.impl;

import dev.breakin.infra.comment.repository.CommentRepository;
import dev.breakin.model.comment.CommentRead;
import dev.breakin.model.comment.CommentIdentity;
import dev.breakin.model.common.CommentOrder;
import dev.breakin.model.common.TargetType;
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
class DefaultCommentReaderTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private DefaultCommentReader commentReader;

    // Sample CommentRead for testing (with nickname)
    private static final CommentRead sampleCommentRead = new CommentRead(
        1L,                          // commentId
        1L,                          // userId
        "testuser",                  // nickname
        "test content",              // content
        TargetType.JOB,              // targetType
        1L,                          // targetId
        null,                        // parentId
        CommentOrder.empty(),        // commentOrder
        false,                       // isHidden
        Instant.now(),               // createdAt
        Instant.now()                // updatedAt
    );

    private final CommentIdentity testIdentity = new CommentIdentity(1L);

    @Test
    void getById_existingId_returnsComment() {
        // given
        when(commentRepository.findById(testIdentity))
            .thenReturn(Optional.of(sampleCommentRead));

        // when
        CommentRead result = commentReader.getById(testIdentity);

        // then
        assertNotNull(result);
        assertEquals(sampleCommentRead.getCommentId(), result.getCommentId());
        assertEquals(sampleCommentRead.getContent(), result.getContent());
        assertEquals(sampleCommentRead.getNickname(), result.getNickname());
        verify(commentRepository).findById(testIdentity);
    }

    @Test
    void getById_nonExistingId_throwsException() {
        // given
        when(commentRepository.findById(testIdentity))
            .thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () ->
            commentReader.getById(testIdentity)
        );
        verify(commentRepository).findById(testIdentity);
    }

    @Test
    void getAll_withData_returnsList() {
        // given
        List<CommentRead> comments = List.of(sampleCommentRead);
        when(commentRepository.findAll())
            .thenReturn(comments);

        // when
        List<CommentRead> result = commentReader.getAll();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleCommentRead.getCommentId(), result.get(0).getCommentId());
        verify(commentRepository).findAll();
    }

    @Test
    void getAll_emptyData_returnsEmptyList() {
        // given
        when(commentRepository.findAll())
            .thenReturn(List.of());

        // when
        List<CommentRead> result = commentReader.getAll();

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(commentRepository).findAll();
    }

    @Test
    void getByTargetTypeAndTargetId_existingData_returnsList() {
        // given
        TargetType targetType = TargetType.JOB;
        Long targetId = 1L;
        List<CommentRead> comments = List.of(sampleCommentRead);
        when(commentRepository.findByTargetTypeAndTargetId(targetType, targetId))
            .thenReturn(comments);

        // when
        var result = commentReader.getByTargetTypeAndTargetId(targetType, targetId);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleCommentRead.getTargetType(), result.get(0).getTargetType());
        assertEquals(sampleCommentRead.getTargetId(), result.get(0).getTargetId());
        verify(commentRepository).findByTargetTypeAndTargetId(targetType, targetId);
    }

    @Test
    void getByTargetTypeAndTargetId_nonExistingData_returnsEmptyList() {
        // given
        TargetType targetType = TargetType.JOB;
        Long targetId = 999L;
        when(commentRepository.findByTargetTypeAndTargetId(targetType, targetId))
            .thenReturn(List.of());

        // when
        List<CommentRead> result = commentReader.getByTargetTypeAndTargetId(targetType, targetId);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(commentRepository).findByTargetTypeAndTargetId(targetType, targetId);
    }

    @Test
    void getByParentId_existingParent_returnsList() {
        // given
        Long parentId = 1L;
        CommentRead replyComment = new CommentRead(
            2L, 1L, "testuser", "reply content", TargetType.JOB, 1L, parentId,
            CommentOrder.empty(), false, Instant.now(), Instant.now()
        );
        List<CommentRead> replies = List.of(replyComment);
        when(commentRepository.findByParentId(parentId))
            .thenReturn(replies);

        // when
        List<CommentRead> result = commentReader.getByParentId(parentId);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(parentId, result.get(0).getParentId());
        verify(commentRepository).findByParentId(parentId);
    }

    @Test
    void getByParentId_nonExistingParent_returnsEmptyList() {
        // given
        Long parentId = 999L;
        when(commentRepository.findByParentId(parentId))
            .thenReturn(List.of());

        // when
        List<CommentRead> result = commentReader.getByParentId(parentId);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(commentRepository).findByParentId(parentId);
    }
}
