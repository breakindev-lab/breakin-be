package dev.breakin.service.comment.impl;

import dev.breakin.infra.comment.repository.CommentRepository;
import dev.breakin.model.comment.Comment;
import dev.breakin.model.comment.CommentIdentity;
import dev.breakin.model.comment.CommentRead;
import dev.breakin.model.common.CommentOrder;
import dev.breakin.model.common.TargetType;
import dev.breakin.service.comment.dto.CommentWriteCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultCommentWriterTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private DefaultCommentWriter commentWriter;

    // Sample Comment for save operations (write)
    private static final Comment sampleComment = new Comment(
        1L,                          // commentId
        1L,                          // userId
        "test content",              // content
        TargetType.JOB,              // targetType
        1L,                          // targetId
        null,                        // parentId
        CommentOrder.empty(),        // commentOrder
        false,                       // isHidden
        Instant.now(),               // createdAt
        Instant.now()                // updatedAt
    );

    // Sample CommentRead for read operations (findById)
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

    @Test
    void write_newComment_callsRepositorySave() {
        // given
        CommentWriteCommand command = new CommentWriteCommand(
            1L,              // userId
            "test content",  // content
            TargetType.JOB,  // targetType
            1L,              // targetId
            null             // parentId (최상위 댓글)
        );
        when(commentRepository.save(any(Comment.class)))
            .thenReturn(sampleComment);

        // when
        Comment result = commentWriter.write(command);

        // then
        assertNotNull(result);
        assertEquals(sampleComment.getContent(), result.getContent());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void write_newReply_callsRepositorySave() {
        // given
        CommentWriteCommand command = new CommentWriteCommand(
            1L,              // userId
            "reply content", // content
            TargetType.JOB,  // targetType
            1L,              // targetId
            1L               // parentId (대댓글)
        );
        Comment replyComment = new Comment(
            2L, 1L, "reply content", TargetType.JOB, 1L, 1L,
            CommentOrder.empty(), false, Instant.now(), Instant.now()
        );
        // 부모 댓글 조회 mock 설정
        when(commentRepository.findById(new CommentIdentity(1L)))
            .thenReturn(Optional.of(sampleCommentRead));
        when(commentRepository.save(any(Comment.class)))
            .thenReturn(replyComment);

        // when
        Comment result = commentWriter.write(command);

        // then
        assertNotNull(result);
        assertEquals(1L, result.getParentId());
        // findById는 2번 호출됨: writeReplyComment에서 1번 + incrementAllParentsChildCount에서 1번
        verify(commentRepository, times(2)).findById(new CommentIdentity(1L));
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void updateComment_existingComment_callsRepositorySave() {
        // given
        Long commentId = 1L;
        String newContent = "updated content";
        Comment updatedComment = new Comment(
            1L, 1L, newContent, TargetType.JOB, 1L, null,
            CommentOrder.empty(), false, Instant.now(), Instant.now()
        );
        when(commentRepository.findById(new CommentIdentity(commentId)))
            .thenReturn(Optional.of(sampleCommentRead));
        when(commentRepository.save(any(Comment.class)))
            .thenReturn(updatedComment);

        // when
        Comment result = commentWriter.updateComment(commentId, newContent);

        // then
        assertNotNull(result);
        assertEquals(newContent, result.getContent());
        verify(commentRepository).findById(new CommentIdentity(commentId));
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void updateComment_nonExistingComment_throwsException() {
        // given
        Long commentId = 999L;
        String newContent = "updated content";
        when(commentRepository.findById(new CommentIdentity(commentId)))
            .thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () ->
            commentWriter.updateComment(commentId, newContent)
        );
        verify(commentRepository).findById(new CommentIdentity(commentId));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void hideComment_existingComment_callsRepositorySave() {
        // given
        Long commentId = 1L;
        Comment hiddenComment = new Comment(
            1L, 1L, "test content", TargetType.JOB, 1L, null,
            CommentOrder.empty(), true, Instant.now(), Instant.now()
        );
        when(commentRepository.findById(new CommentIdentity(commentId)))
            .thenReturn(Optional.of(sampleCommentRead));
        when(commentRepository.save(any(Comment.class)))
            .thenReturn(hiddenComment);

        // when
        Comment result = commentWriter.hideComment(commentId);

        // then
        assertNotNull(result);
        assertTrue(result.getIsHidden());
        verify(commentRepository).findById(new CommentIdentity(commentId));
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void hideComment_nonExistingComment_throwsException() {
        // given
        Long commentId = 999L;
        when(commentRepository.findById(new CommentIdentity(commentId)))
            .thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () ->
            commentWriter.hideComment(commentId)
        );
        verify(commentRepository).findById(new CommentIdentity(commentId));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void showComment_existingComment_callsRepositorySave() {
        // given
        Long commentId = 1L;
        // isHidden=true인 댓글로 테스트 (show할 수 있는 상태)
        CommentRead hiddenCommentRead = new CommentRead(
            1L, 1L, "testuser", "test content", TargetType.JOB, 1L, null,
            CommentOrder.empty(), true, Instant.now(), Instant.now()
        );
        when(commentRepository.findById(new CommentIdentity(commentId)))
            .thenReturn(Optional.of(hiddenCommentRead));
        when(commentRepository.save(any(Comment.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Comment result = commentWriter.showComment(commentId);

        // then
        assertNotNull(result);
        assertFalse(result.getIsHidden());
        verify(commentRepository).findById(new CommentIdentity(commentId));
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void showComment_nonExistingComment_throwsException() {
        // given
        Long commentId = 999L;
        when(commentRepository.findById(new CommentIdentity(commentId)))
            .thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () ->
            commentWriter.showComment(commentId)
        );
        verify(commentRepository).findById(new CommentIdentity(commentId));
        verify(commentRepository, never()).save(any(Comment.class));
    }
}
