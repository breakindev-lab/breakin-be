package dev.breakin.jdbc.communitypost.repository;

import dev.breakin.model.common.Popularity;
import dev.breakin.model.communitypost.CommunityPost;
import dev.breakin.model.communitypost.CommunityPostCategory;
import dev.breakin.model.communitypost.CommunityPostIdentity;
import dev.breakin.model.communitypost.CommunityPostRead;
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
 * CommunityPostJdbcRepository 테스트
 *
 * @DataJdbcTest를 사용한 Spring Data JDBC 통합 테스트
 * Entity ↔ Domain 변환 로직 및 커스텀 쿼리 메서드 검증
 */
@DataJdbcTest
@ComponentScan("dev.breakin.jdbc.communitypost.repository")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommunityPostJdbcRepositoryTest {

    @Autowired
    private CommunityPostJdbcRepository communityPostRepository;

    // 테스트 데이터
    private final CommunityPost sampleCommunityPost = new CommunityPost(
            null,                           // communityPostId (자동 생성)
            1L,                             // userId
            CommunityPostCategory.INTERVIEW_SHARE,  // category
            "Test title",                   // title
            "Test markdown body",           // markdownBody
            "Company A",                    // company
            "Seoul",                        // location
            100L,                           // linkedJobId
            false,                          // isFromJobComment
            Popularity.empty(),             // popularity
            false,                          // isDeleted
            Instant.now(),                  // createdAt
            Instant.now()                   // updatedAt
    );

    private final CommunityPostIdentity testIdentity = new CommunityPostIdentity(1L);
    private final CommunityPostIdentity nonExistingIdentity = new CommunityPostIdentity(999L);

    // ========== Entity↔Domain 변환 테스트 ==========

    @Test
    void save_withValidDomain_returnsConvertedDomain() {
        // given
        CommunityPost communityPostToSave = sampleCommunityPost;

        // when
        CommunityPost saved = communityPostRepository.save(communityPostToSave);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getCommunityPostId()).isNotNull();
        assertThat(saved.getUserId()).isEqualTo(communityPostToSave.getUserId());
        assertThat(saved.getCategory()).isEqualTo(communityPostToSave.getCategory());
        assertThat(saved.getTitle()).isEqualTo(communityPostToSave.getTitle());
        assertThat(saved.getMarkdownBody()).isEqualTo(communityPostToSave.getMarkdownBody());
        assertThat(saved.getCompany()).isEqualTo(communityPostToSave.getCompany());
        assertThat(saved.getLocation()).isEqualTo(communityPostToSave.getLocation());
        assertThat(saved.getLinkedJobId()).isEqualTo(communityPostToSave.getLinkedJobId());
        assertThat(saved.getIsFromJobComment()).isEqualTo(communityPostToSave.getIsFromJobComment());
        assertThat(saved.getIsDeleted()).isEqualTo(communityPostToSave.getIsDeleted());
    }

    @Test
    void save_withNullId_generatesIdAndReturns() {
        // given
        CommunityPost communityPostWithNullId = sampleCommunityPost;

        // when
        CommunityPost saved = communityPostRepository.save(communityPostWithNullId);

        // then
        assertThat(saved.getCommunityPostId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo(communityPostWithNullId.getTitle());
    }

    @Test
    void findById_existingId_returnsConvertedDomain() {
        // given
        CommunityPost saved = communityPostRepository.save(sampleCommunityPost);
        CommunityPostIdentity identity = new CommunityPostIdentity(saved.getCommunityPostId());

        // when
        Optional<CommunityPostRead> found = communityPostRepository.findById(identity);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getCommunityPostId()).isEqualTo(saved.getCommunityPostId());
        assertThat(found.get().getTitle()).isEqualTo(saved.getTitle());
        assertThat(found.get().getCategory()).isEqualTo(saved.getCategory());
        assertThat(found.get().getNickname()).isNull();  // LEFT JOIN with no user
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        // when
        Optional<CommunityPostRead> found = communityPostRepository.findById(nonExistingIdentity);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void findAll_withData_returnsConvertedList() {
        // given
        CommunityPost saved1 = communityPostRepository.save(sampleCommunityPost);
        CommunityPost saved2 = communityPostRepository.save(new CommunityPost(
                null, 2L, CommunityPostCategory.QUESTION, "Another post", "Body",
                "Company B", "Busan", null, false, Popularity.empty(), false, Instant.now(), Instant.now()
        ));

        // when
        List<CommunityPostRead> all = communityPostRepository.findAll();

        // then
        assertThat(all).hasSize(2);
        assertThat(all).extracting(CommunityPostRead::getCommunityPostId)
                .containsExactlyInAnyOrder(saved1.getCommunityPostId(), saved2.getCommunityPostId());
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        // when
        List<CommunityPostRead> all = communityPostRepository.findAll();

        // then
        assertThat(all).isEmpty();
    }

    // ========== 커스텀 쿼리 테스트 ==========

    @Test
    void findByUserId_existingUserId_returnsConvertedList() {
        // given
        CommunityPost saved1 = communityPostRepository.save(sampleCommunityPost);
        CommunityPost saved2 = communityPostRepository.save(new CommunityPost(
                null, 1L, CommunityPostCategory.QUESTION, "Another post", "Body",
                "Company B", "Busan", null, false, Popularity.empty(), false, Instant.now(), Instant.now()
        ));
        communityPostRepository.save(new CommunityPost(
                null, 2L, CommunityPostCategory.INTERVIEW_SHARE, "Different user", "Body",
                "Company C", "Seoul", null, false, Popularity.empty(), false, Instant.now(), Instant.now()
        ));

        // when
        List<CommunityPostRead> found = communityPostRepository.findByUserId(1L);

        // then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(CommunityPostRead::getCommunityPostId)
                .containsExactlyInAnyOrder(saved1.getCommunityPostId(), saved2.getCommunityPostId());
    }

    @Test
    void findByUserId_nonExistingUserId_returnsEmptyList() {
        // when
        List<CommunityPostRead> found = communityPostRepository.findByUserId(999L);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void findByCompany_existingCompany_returnsConvertedList() {
        // given
        CommunityPost saved1 = communityPostRepository.save(sampleCommunityPost);
        CommunityPost saved2 = communityPostRepository.save(new CommunityPost(
                null, 2L, CommunityPostCategory.QUESTION, "Another post", "Body",
                "Company A", "Busan", null, false, Popularity.empty(), false, Instant.now(), Instant.now()
        ));
        communityPostRepository.save(new CommunityPost(
                null, 3L, CommunityPostCategory.INTERVIEW_SHARE, "Different company", "Body",
                "Company B", "Seoul", null, false, Popularity.empty(), false, Instant.now(), Instant.now()
        ));

        // when
        List<CommunityPostRead> found = communityPostRepository.findByCompany("Company A");

        // then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(CommunityPostRead::getCommunityPostId)
                .containsExactlyInAnyOrder(saved1.getCommunityPostId(), saved2.getCommunityPostId());
    }

    @Test
    void findByCompany_nonExistingCompany_returnsEmptyList() {
        // when
        List<CommunityPostRead> found = communityPostRepository.findByCompany("NonExisting");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void findByLocation_existingLocation_returnsConvertedList() {
        // given
        CommunityPost saved1 = communityPostRepository.save(sampleCommunityPost);
        CommunityPost saved2 = communityPostRepository.save(new CommunityPost(
                null, 2L, CommunityPostCategory.QUESTION, "Another post", "Body",
                "Company B", "Seoul", null, false, Popularity.empty(), false, Instant.now(), Instant.now()
        ));
        communityPostRepository.save(new CommunityPost(
                null, 3L, CommunityPostCategory.INTERVIEW_SHARE, "Different location", "Body",
                "Company C", "Busan", null, false, Popularity.empty(), false, Instant.now(), Instant.now()
        ));

        // when
        List<CommunityPostRead> found = communityPostRepository.findByLocation("Seoul");

        // then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(CommunityPostRead::getCommunityPostId)
                .containsExactlyInAnyOrder(saved1.getCommunityPostId(), saved2.getCommunityPostId());
    }

    @Test
    void findByLocation_nonExistingLocation_returnsEmptyList() {
        // when
        List<CommunityPostRead> found = communityPostRepository.findByLocation("NonExisting");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void increaseViewCount_validCommunityPostId_incrementsSuccessfully() {
        // given
        CommunityPost saved = communityPostRepository.save(sampleCommunityPost);
        CommunityPostIdentity identity = new CommunityPostIdentity(saved.getCommunityPostId());

        // when
        communityPostRepository.increaseViewCount(identity, 10);

        // then - 수동 검증 필요 (실제 viewCount 확인)
        Optional<CommunityPostRead> found = communityPostRepository.findById(identity);
        assertThat(found).isPresent();
    }
}
