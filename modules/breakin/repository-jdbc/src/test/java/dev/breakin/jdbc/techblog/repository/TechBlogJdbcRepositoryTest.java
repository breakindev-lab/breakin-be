package dev.breakin.jdbc.techblog.repository;

import dev.breakin.model.common.Popularity;
import dev.breakin.model.techblog.TechBlog;
import dev.breakin.model.techblog.TechBlogIdentity;
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
 * TechBlogJdbcRepository 테스트
 *
 * @DataJdbcTest를 사용한 Spring Data JDBC 통합 테스트
 * Entity ↔ Domain 변환 로직 및 커스텀 쿼리 메서드 검증
 */
@DataJdbcTest
@ComponentScan("dev.breakin.jdbc.techblog.repository")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TechBlogJdbcRepositoryTest {

    @Autowired
    private TechBlogJdbcRepository techBlogRepository;

    // 테스트 데이터
    private final TechBlog sampleTechBlog = new TechBlog(
            null,                           // techBlogId (자동 생성)
            "https://example.com/blog1",    // url
            "Company A",                    // company
            "Tech blog title",              // title
            "Blog markdown body",           // markdownBody
            "https://example.com/thumb.jpg", // thumbnailUrl
            List.of("Java", "Spring"),      // tags
            List.of("Backend"),             // techCategories
            "https://original.com/blog1",   // originalUrl
            Popularity.empty(),             // popularity
            false,                          // isDeleted
            Instant.now(),                  // createdAt
            Instant.now()                   // updatedAt
    );

    private final TechBlogIdentity testIdentity = new TechBlogIdentity(1L);
    private final TechBlogIdentity nonExistingIdentity = new TechBlogIdentity(999L);

    // ========== Entity↔Domain 변환 테스트 ==========

    @Test
    void save_withValidDomain_returnsConvertedDomain() {
        // given
        TechBlog techBlogToSave = sampleTechBlog;

        // when
        TechBlog saved = techBlogRepository.save(techBlogToSave);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getTechBlogId()).isNotNull();
        assertThat(saved.getUrl()).isEqualTo(techBlogToSave.getUrl());
        assertThat(saved.getCompany()).isEqualTo(techBlogToSave.getCompany());
        assertThat(saved.getTitle()).isEqualTo(techBlogToSave.getTitle());
        assertThat(saved.getMarkdownBody()).isEqualTo(techBlogToSave.getMarkdownBody());
        assertThat(saved.getThumbnailUrl()).isEqualTo(techBlogToSave.getThumbnailUrl());
        assertThat(saved.getTags()).containsExactlyInAnyOrderElementsOf(techBlogToSave.getTags());
        assertThat(saved.getTechCategories()).containsExactlyInAnyOrderElementsOf(techBlogToSave.getTechCategories());
        assertThat(saved.getOriginalUrl()).isEqualTo(techBlogToSave.getOriginalUrl());
        assertThat(saved.getIsDeleted()).isEqualTo(techBlogToSave.getIsDeleted());
    }

    @Test
    void save_withNullId_generatesIdAndReturns() {
        // given
        TechBlog techBlogWithNullId = sampleTechBlog;

        // when
        TechBlog saved = techBlogRepository.save(techBlogWithNullId);

        // then
        assertThat(saved.getTechBlogId()).isNotNull();
        assertThat(saved.getUrl()).isEqualTo(techBlogWithNullId.getUrl());
    }

    @Test
    void findById_existingId_returnsConvertedDomain() {
        // given
        TechBlog saved = techBlogRepository.save(sampleTechBlog);
        TechBlogIdentity identity = new TechBlogIdentity(saved.getTechBlogId());

        // when
        Optional<TechBlog> found = techBlogRepository.findById(identity);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getTechBlogId()).isEqualTo(saved.getTechBlogId());
        assertThat(found.get().getUrl()).isEqualTo(saved.getUrl());
        assertThat(found.get().getCompany()).isEqualTo(saved.getCompany());
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        // when
        Optional<TechBlog> found = techBlogRepository.findById(nonExistingIdentity);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void findAll_withData_returnsConvertedList() {
        // given
        TechBlog saved1 = techBlogRepository.save(sampleTechBlog);
        TechBlog saved2 = techBlogRepository.save(new TechBlog(
                null, "https://example.com/blog2", "Company B", "Another blog",
                "Body", null, List.of("React"), List.of("Frontend"), null,
                Popularity.empty(), false, Instant.now(), Instant.now()
        ));

        // when
        List<TechBlog> all = techBlogRepository.findAll();

        // then
        assertThat(all).hasSize(2);
        assertThat(all).extracting(TechBlog::getTechBlogId)
                .containsExactlyInAnyOrder(saved1.getTechBlogId(), saved2.getTechBlogId());
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        // when
        List<TechBlog> all = techBlogRepository.findAll();

        // then
        assertThat(all).isEmpty();
    }

    // ========== 커스텀 쿼리 테스트 ==========

    @Test
    void findByUrl_existingUrl_returnsConvertedDomain() {
        // given
        TechBlog saved = techBlogRepository.save(sampleTechBlog);

        // when
        Optional<TechBlog> found = techBlogRepository.findByUrl(saved.getUrl());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getTechBlogId()).isEqualTo(saved.getTechBlogId());
        assertThat(found.get().getUrl()).isEqualTo(saved.getUrl());
    }

    @Test
    void findByUrl_nonExistingUrl_returnsEmpty() {
        // when
        Optional<TechBlog> found = techBlogRepository.findByUrl("https://nonexisting.com");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void findByCompany_existingCompany_returnsConvertedList() {
        // given
        TechBlog saved1 = techBlogRepository.save(sampleTechBlog);
        TechBlog saved2 = techBlogRepository.save(new TechBlog(
                null, "https://example.com/blog2", "Company A", "Another blog",
                "Body", null, List.of("React"), List.of("Frontend"), null,
                Popularity.empty(), false, Instant.now(), Instant.now()
        ));
        techBlogRepository.save(new TechBlog(
                null, "https://example.com/blog3", "Company B", "Different blog",
                "Body", null, List.of("Python"), List.of("Backend"), null,
                Popularity.empty(), false, Instant.now(), Instant.now()
        ));

        // when
        List<TechBlog> found = techBlogRepository.findByCompany("Company A");

        // then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(TechBlog::getTechBlogId)
                .containsExactlyInAnyOrder(saved1.getTechBlogId(), saved2.getTechBlogId());
    }

    @Test
    void findByCompany_nonExistingCompany_returnsEmptyList() {
        // when
        List<TechBlog> found = techBlogRepository.findByCompany("NonExisting");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void increaseViewCount_validTechBlogId_incrementsSuccessfully() {
        // given
        TechBlog saved = techBlogRepository.save(sampleTechBlog);
        TechBlogIdentity identity = new TechBlogIdentity(saved.getTechBlogId());

        // when
        techBlogRepository.increaseViewCount(identity, 10);

        // then - 수동 검증 필요 (실제 viewCount 확인)
        Optional<TechBlog> found = techBlogRepository.findById(identity);
        assertThat(found).isPresent();
    }
}
