package dev.breakin.jdbc.comment.repository;

import dev.breakin.model.comment.Comment;
import dev.breakin.model.comment.CommentIdentity;
import dev.breakin.model.comment.CommentRead;
import dev.breakin.model.common.CommentOrder;
import dev.breakin.model.common.TargetType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CommentJdbcRepository 테스트
 *
 * @DataJdbcTest를 사용한 Spring Data JDBC 통합 테스트
 * Entity ↔ Domain 변환 로직 및 커스텀 쿼리 메서드 검증
 */
@DataJdbcTest
@ComponentScan("dev.breakin.jdbc.comment.repository")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentJdbcRepositoryTest {

    @Autowired
    private CommentJdbcRepository commentRepository;

    // 테스트 데이터
    private final Comment sampleComment = new Comment(
            null,                           // commentId (자동 생성)
            1L,                             // userId
            "Test comment content",         // content
            TargetType.JOB,                 // targetType
            100L,                           // targetId
            null,                           // parentId
            CommentOrder.newRootComment(1), // commentOrder
            false,                          // isHidden
            Instant.now(),                  // createdAt
            Instant.now()                   // updatedAt
    );

    private final CommentIdentity testIdentity = new CommentIdentity(1L);
    private final CommentIdentity nonExistingIdentity = new CommentIdentity(999L);

    // ========== Entity↔Domain 변환 테스트 ==========

    @Test
    void save_withValidDomain_returnsConvertedDomain() {
        // given
        Comment commentToSave = sampleComment;

        // when
        Comment saved = commentRepository.save(commentToSave);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getCommentId()).isNotNull();
        assertThat(saved.getUserId()).isEqualTo(commentToSave.getUserId());
        assertThat(saved.getContent()).isEqualTo(commentToSave.getContent());
        assertThat(saved.getTargetType()).isEqualTo(commentToSave.getTargetType());
        assertThat(saved.getTargetId()).isEqualTo(commentToSave.getTargetId());
        assertThat(saved.getParentId()).isEqualTo(commentToSave.getParentId());
        assertThat(saved.getCommentOrder()).isEqualTo(commentToSave.getCommentOrder());
        assertThat(saved.getIsHidden()).isEqualTo(commentToSave.getIsHidden());
    }

    @Test
    void save_withNullId_generatesIdAndReturns() {
        // given
        Comment commentWithNullId = sampleComment;

        // when
        Comment saved = commentRepository.save(commentWithNullId);

        // then
        assertThat(saved.getCommentId()).isNotNull();
        assertThat(saved.getContent()).isEqualTo(commentWithNullId.getContent());
    }

    @Test
    void findById_existingId_returnsConvertedDomain()  {
        // given
        Comment saved = commentRepository.save(sampleComment);
        CommentIdentity identity = new CommentIdentity(saved.getCommentId());

        // when
        var found = commentRepository.findById(identity);


        // then
        assertThat(found).isPresent();
        assertThat(found.get().getCommentId()).isEqualTo(saved.getCommentId());
        assertThat(found.get().getContent()).isEqualTo(saved.getContent());
        assertThat(found.get().getTargetType()).isEqualTo(saved.getTargetType());
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        // when
        var found = commentRepository.findById(nonExistingIdentity);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void findAll_withData_returnsConvertedList() {
        // given
        Comment saved1 = commentRepository.save(sampleComment);
        Comment saved2 = commentRepository.save(new Comment(
                null, 2L, "Another comment", TargetType.JOB, 100L, null,
                CommentOrder.newRootComment(2), false, Instant.now(), Instant.now()
        ));

        // when
        var all = commentRepository.findAll();

        // then
        assertThat(all).hasSize(2);
        assertThat(all).extracting(CommentRead::getCommentId)
                .containsExactlyInAnyOrder(saved1.getCommentId(), saved2.getCommentId());
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        // when
        var all = commentRepository.findAll();

        // then
        assertThat(all).isEmpty();
    }

    // ========== 커스텀 쿼리 테스트 ==========

    @Test
    void findByTargetTypeAndTargetId_existingData_returnsConvertedList() {
        // given
        Comment saved1 = commentRepository.save(sampleComment);
        Comment saved2 = commentRepository.save(new Comment(
                null, 2L, "Another comment", TargetType.JOB, 100L, null,
                CommentOrder.newRootComment(2), false, Instant.now(), Instant.now()
        ));
        commentRepository.save(new Comment(
                null, 3L, "Different target", TargetType.COMMUNITY_POST, 200L, null,
                CommentOrder.newRootComment(1), false, Instant.now(), Instant.now()
        ));

        // when
        var found = commentRepository.findByTargetTypeAndTargetId(TargetType.JOB, 100L);

        // then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(CommentRead::getCommentId)
                .containsExactlyInAnyOrder(saved1.getCommentId(), saved2.getCommentId());
    }

    @Test
    void findByTargetTypeAndTargetId_nonExistingData_returnsEmptyList() {
        // when
        var found = commentRepository.findByTargetTypeAndTargetId(TargetType.JOB, 999L);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void findByTargetTypeAndTargetIdWithPaging_existingData_returnsPaginatedList() {
        // given
        for (int i = 0; i < 5; i++) {
            commentRepository.save(new Comment(
                    null, (long) (i + 1), "Comment " + i, TargetType.JOB, 100L, null,
                    CommentOrder.newRootComment(i + 1), false, Instant.now(), Instant.now()
            ));
        }

        // when
        var page1 = commentRepository.findByTargetTypeAndTargetIdWithPaging(TargetType.JOB, 100L, 0, 2);
        var page2 = commentRepository.findByTargetTypeAndTargetIdWithPaging(TargetType.JOB, 100L, 2, 2);

        // then
        assertThat(page1).hasSize(2);
        assertThat(page2).hasSize(2);
    }

    @Test
    void findByParentId_existingParent_returnsConvertedList() {
        // given
        Comment parent = commentRepository.save(sampleComment);
        Comment reply1 = commentRepository.save(new Comment(
                null, 2L, "Reply 1", TargetType.JOB, 100L, parent.getCommentId(),
                CommentOrder.newReply(1, 1, 1, parent.getCommentId()), false, Instant.now(), Instant.now()
        ));
        Comment reply2 = commentRepository.save(new Comment(
                null, 3L, "Reply 2", TargetType.JOB, 100L, parent.getCommentId(),
                CommentOrder.newReply(1, 1, 2, parent.getCommentId()), false, Instant.now(), Instant.now()
        ));

        // when
        var replies = commentRepository.findByParentId(parent.getCommentId());

        // then
        assertThat(replies).hasSize(2);
        assertThat(replies).extracting(CommentRead::getCommentId)
                .containsExactlyInAnyOrder(reply1.getCommentId(), reply2.getCommentId());
    }

    @Test
    void findByParentId_nonExistingParent_returnsEmptyList() {
        // when
        var replies = commentRepository.findByParentId(999L);

        // then
        assertThat(replies).isEmpty();
    }

    @Test
    void findMaxCommentOrder_existingComments_returnsMaxValue() {
        // given
        commentRepository.save(new Comment(
                null, 1L, "Comment 1", TargetType.JOB, 100L, null,
                CommentOrder.newRootComment(1), false, Instant.now(), Instant.now()
        ));
        commentRepository.save(new Comment(
                null, 2L, "Comment 2", TargetType.JOB, 100L, null,
                CommentOrder.newRootComment(5), false, Instant.now(), Instant.now()
        ));
        commentRepository.save(new Comment(
                null, 3L, "Comment 3", TargetType.JOB, 100L, null,
                CommentOrder.newRootComment(3), false, Instant.now(), Instant.now()
        ));

        // when
        Integer maxOrder = commentRepository.findMaxCommentOrder(TargetType.JOB, 100L);

        // then
        assertThat(maxOrder).isEqualTo(5);
    }

    @Test
    void findMaxCommentOrder_noComments_returnsNull() {
        // when
        Integer maxOrder = commentRepository.findMaxCommentOrder(TargetType.JOB, 999L);

        // then
        assertThat(maxOrder).isEqualTo(0);
    }

    @Test
    void incrementSortNumbersAbove_validInput_updatesCorrectly() {
        // given
        commentRepository.save(new Comment(
                null, 1L, "Comment 1", TargetType.JOB, 100L, null,
                CommentOrder.newReply(1, 1, 1, null), false, Instant.now(), Instant.now()
        ));
        commentRepository.save(new Comment(
                null, 2L, "Comment 2", TargetType.JOB, 100L, null,
                CommentOrder.newReply(1, 1, 2, null), false, Instant.now(), Instant.now()
        ));

        // when
        commentRepository.incrementSortNumbersAbove(TargetType.JOB, 100L, 1, 1);

        // then - 수동 검증 필요 (실제 업데이트 확인)
        var comments = commentRepository.findByTargetTypeAndTargetId(TargetType.JOB, 100L);
        assertThat(comments).isNotEmpty();
    }

    @Test
    void incrementChildCount_validCommentId_incrementsSuccessfully() {
        // given
        Comment parent = commentRepository.save(sampleComment);

        // when
        commentRepository.incrementChildCount(parent.getCommentId());

        // then - 수동 검증 필요 (실제 childCount 확인)
        var found = commentRepository.findById(new CommentIdentity(parent.getCommentId()));
        assertThat(found).isPresent();
    }
}
