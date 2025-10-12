package dev.breakin.service.comment.impl;

import dev.breakin.model.comment.CommentIdentity;
import dev.breakin.model.comment.CommentRead;
import dev.breakin.service.comment.CommentReader;
import dev.breakin.infra.comment.repository.CommentRepository;
import dev.breakin.model.common.TargetType;
import dev.breakin.exception.comment.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Comment 도메인 조회 서비스 구현체
 *
 * CQRS 패턴의 Query 책임을 구현하며,
 * Infrastructure Repository를 활용한 조회 로직을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultCommentReader implements CommentReader {

    private final CommentRepository commentRepository;

    @Override
    public CommentRead getById(CommentIdentity identity) {
        log.debug("Fetching Comment by id: {}", identity.getCommentId());
        return commentRepository.findById(identity)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + identity.getCommentId() + " not found"));
    }

    @Override
    public List<CommentRead> getAll() {
        log.debug("Fetching all Comments");
        return commentRepository.findAll();
    }

    @Override
    public List<CommentRead> getByTargetTypeAndTargetId(TargetType targetType, Long targetId) {
        log.debug("Fetching Comments by targetType: {} and targetId: {}", targetType, targetId);
        return commentRepository.findByTargetTypeAndTargetId(targetType, targetId);
    }

    @Override
    public List<CommentRead> getByParentId(Long parentId) {
        log.debug("Fetching Comments by parentId: {}", parentId);
        return commentRepository.findByParentId(parentId);
    }
}
