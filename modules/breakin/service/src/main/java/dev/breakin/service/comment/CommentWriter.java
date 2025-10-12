package dev.breakin.service.comment;

import dev.breakin.model.comment.Comment;
import dev.breakin.service.comment.dto.CommentWriteCommand;

/**
 * Comment 비즈니스 로직 서비스
 *
 * 계층형 댓글 구조를 관리하며, CommentOrder 기반의 정렬을 지원합니다.
 */
public interface CommentWriter {

    /**
     * 댓글 작성 (최상위 댓글 또는 대댓글)
     *
     * @param command 댓글 작성 커맨드 (parentId가 null이면 최상위 댓글)
     * @return 생성된 Comment
     */
    Comment write(CommentWriteCommand command);

    /**
     * 댓글 수정
     *
     * @param commentId 댓글 ID
     * @param newContent 새로운 내용
     * @return 수정된 Comment
     */
    Comment updateComment(Long commentId, String newContent);

    /**
     * 댓글 숨김 처리
     *
     * @param commentId 댓글 ID
     * @return 숨김 처리된 Comment
     */
    Comment hideComment(Long commentId);

    /**
     * 댓글 숨김 해제
     *
     * @param commentId 댓글 ID
     * @return 숨김 해제된 Comment
     */
    Comment showComment(Long commentId);
}
