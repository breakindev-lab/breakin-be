package dev.breakin.jdbc.reaction.repository;

import dev.breakin.model.common.TargetType;
import dev.breakin.model.reaction.Reaction;
import dev.breakin.model.reaction.ReactionIdentity;
import dev.breakin.model.reaction.ReactionType;
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
 * ReactionJdbcRepository 테스트
 *
 * @DataJdbcTest를 사용한 Spring Data JDBC 통합 테스트
 * Entity ↔ Domain 변환 로직 및 커스텀 쿼리 메서드 검증
 */
@DataJdbcTest
@ComponentScan("dev.breakin.jdbc.reaction.repository")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReactionJdbcRepositoryTest {

    @Autowired
    private ReactionJdbcRepository reactionRepository;

    // 테스트 데이터
    private final Reaction sampleReaction = new Reaction(
            null,                           // reactionId (자동 생성)
            1L,                             // userId
            TargetType.JOB,                 // targetType
            100L,                           // targetId
            ReactionType.LIKE,              // reactionType
            Instant.now(),                  // createdAt
            Instant.now()                   // updatedAt
    );

    private final ReactionIdentity testIdentity = new ReactionIdentity(1L);
    private final ReactionIdentity nonExistingIdentity = new ReactionIdentity(999L);

    // ========== Entity↔Domain 변환 테스트 ==========

    @Test
    void save_withValidDomain_returnsConvertedDomain() {
        // given
        Reaction reactionToSave = sampleReaction;

        // when
        Reaction saved = reactionRepository.save(reactionToSave);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getReactionId()).isNotNull();
        assertThat(saved.getUserId()).isEqualTo(reactionToSave.getUserId());
        assertThat(saved.getTargetType()).isEqualTo(reactionToSave.getTargetType());
        assertThat(saved.getTargetId()).isEqualTo(reactionToSave.getTargetId());
        assertThat(saved.getReactionType()).isEqualTo(reactionToSave.getReactionType());
    }

    @Test
    void save_withNullId_generatesIdAndReturns() {
        // given
        Reaction reactionWithNullId = sampleReaction;

        // when
        Reaction saved = reactionRepository.save(reactionWithNullId);

        // then
        assertThat(saved.getReactionId()).isNotNull();
        assertThat(saved.getUserId()).isEqualTo(reactionWithNullId.getUserId());
    }

    @Test
    void findById_existingId_returnsConvertedDomain() {
        // given
        Reaction saved = reactionRepository.save(sampleReaction);
        ReactionIdentity identity = new ReactionIdentity(saved.getReactionId());

        // when
        Optional<Reaction> found = reactionRepository.findById(identity);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getReactionId()).isEqualTo(saved.getReactionId());
        assertThat(found.get().getUserId()).isEqualTo(saved.getUserId());
        assertThat(found.get().getTargetType()).isEqualTo(saved.getTargetType());
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        // when
        Optional<Reaction> found = reactionRepository.findById(nonExistingIdentity);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void findAll_withData_returnsConvertedList() {
        // given
        Reaction saved1 = reactionRepository.save(sampleReaction);
        Reaction saved2 = reactionRepository.save(new Reaction(
                null, 2L, TargetType.COMMUNITY_POST, 200L, ReactionType.DISLIKE,
                Instant.now(), Instant.now()
        ));

        // when
        List<Reaction> all = reactionRepository.findAll();

        // then
        assertThat(all).hasSize(2);
        assertThat(all).extracting(Reaction::getReactionId)
                .containsExactlyInAnyOrder(saved1.getReactionId(), saved2.getReactionId());
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        // when
        List<Reaction> all = reactionRepository.findAll();

        // then
        assertThat(all).isEmpty();
    }

    // ========== 커스텀 쿼리 테스트 ==========

    @Test
    void findByUserId_existingUserId_returnsConvertedList() {
        // given
        Reaction saved1 = reactionRepository.save(sampleReaction);
        Reaction saved2 = reactionRepository.save(new Reaction(
                null, 1L, TargetType.COMMUNITY_POST, 200L, ReactionType.DISLIKE,
                Instant.now(), Instant.now()
        ));
        reactionRepository.save(new Reaction(
                null, 2L, TargetType.JOB, 100L, ReactionType.LIKE,
                Instant.now(), Instant.now()
        ));

        // when
        List<Reaction> found = reactionRepository.findByUserId(1L);

        // then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(Reaction::getReactionId)
                .containsExactlyInAnyOrder(saved1.getReactionId(), saved2.getReactionId());
    }

    @Test
    void findByUserId_nonExistingUserId_returnsEmptyList() {
        // when
        List<Reaction> found = reactionRepository.findByUserId(999L);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void findByTargetTypeAndTargetId_existingData_returnsConvertedList() {
        // given
        Reaction saved1 = reactionRepository.save(sampleReaction);
        Reaction saved2 = reactionRepository.save(new Reaction(
                null, 2L, TargetType.JOB, 100L, ReactionType.DISLIKE,
                Instant.now(), Instant.now()
        ));
        reactionRepository.save(new Reaction(
                null, 3L, TargetType.COMMUNITY_POST, 200L, ReactionType.LIKE,
                Instant.now(), Instant.now()
        ));

        // when
        List<Reaction> found = reactionRepository.findByTargetTypeAndTargetId(TargetType.JOB, 100L);

        // then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(Reaction::getReactionId)
                .containsExactlyInAnyOrder(saved1.getReactionId(), saved2.getReactionId());
    }

    @Test
    void findByTargetTypeAndTargetId_nonExistingData_returnsEmptyList() {
        // when
        List<Reaction> found = reactionRepository.findByTargetTypeAndTargetId(TargetType.JOB, 999L);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void findByUserIdAndTargetTypeAndTargetId_existingData_returnsConvertedDomain() {
        // given
        Reaction saved = reactionRepository.save(sampleReaction);

        // when
        Optional<Reaction> found = reactionRepository.findByUserIdAndTargetTypeAndTargetId(
                1L, TargetType.JOB, 100L
        );

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getReactionId()).isEqualTo(saved.getReactionId());
        assertThat(found.get().getUserId()).isEqualTo(saved.getUserId());
    }

    @Test
    void findByUserIdAndTargetTypeAndTargetId_nonExistingData_returnsEmpty() {
        // when
        Optional<Reaction> found = reactionRepository.findByUserIdAndTargetTypeAndTargetId(
                999L, TargetType.JOB, 100L
        );

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void countByTargetTypeAndTargetIdAndReactionType_existingData_returnsCount() {
        // given
        reactionRepository.save(sampleReaction);
        reactionRepository.save(new Reaction(
                null, 2L, TargetType.JOB, 100L, ReactionType.LIKE,
                Instant.now(), Instant.now()
        ));
        reactionRepository.save(new Reaction(
                null, 3L, TargetType.JOB, 100L, ReactionType.DISLIKE,
                Instant.now(), Instant.now()
        ));

        // when
        long count = reactionRepository.countByTargetTypeAndTargetIdAndReactionType(
                TargetType.JOB, 100L, ReactionType.LIKE
        );

        // then
        assertThat(count).isEqualTo(2);
    }

    @Test
    void countByTargetTypeAndTargetIdAndReactionType_noData_returnsZero() {
        // when
        long count = reactionRepository.countByTargetTypeAndTargetIdAndReactionType(
                TargetType.JOB, 999L, ReactionType.LIKE
        );

        // then
        assertThat(count).isZero();
    }
}
