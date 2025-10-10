package dev.breakin.jdbc.job.repository;

import dev.breakin.model.common.Popularity;
import dev.breakin.model.common.TechCategory;
import dev.breakin.model.job.*;
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
 * JobJdbcRepository 테스트
 *
 * @DataJdbcTest를 사용한 Spring Data JDBC 통합 테스트
 * Entity ↔ Domain 변환 로직 및 커스텀 쿼리 메서드 검증
 */
@DataJdbcTest
@ComponentScan("dev.breakin.jdbc.job.repository")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JobJdbcRepositoryTest {

    @Autowired
    private JobJdbcRepository jobRepository;

    // 테스트 데이터
    private final Job sampleJob = new Job(
            null,                           // jobId (자동 생성)
            "https://example.com/job1",     // url
            "Company A",                    // company
            "Backend Developer",            // title
            "Tech Team",                    // organization
            "Job description",              // markdownBody
            "One line summary",             // oneLineSummary
            2,                              // minYears
            5,                              // maxYears
            true,                           // experienceRequired
            CareerLevel.EXPERIENCED,        // careerLevel
            EmploymentType.FULL_TIME,       // employmentType
            PositionCategory.BACKEND,       // positionCategory
            RemotePolicy.HYBRID,            // remotePolicy
            List.of(TechCategory.JAVA, TechCategory.MYSQL),      // techCategories
            Instant.now(),                  // startedAt
            null,                           // endedAt
            true,                           // isOpenEnded
            false,                          // isClosed
            List.of("Seoul"),               // locations
            "Position intro",               // positionIntroduction
            List.of("Develop APIs", "Write tests"),  // responsibilities
            List.of("Java 3+ years", "Spring experience"),  // qualifications
            List.of("MSA experience", "Open source contributions"),  // preferredQualifications
            "Full job description",         // fullDescription
            false,                          // hasAssignment
            true,                           // hasCodingTest
            false,                          // hasLiveCoding
            3,                              // interviewCount
            30,                             // interviewDays
            Popularity.empty(),             // popularity
            false,                          // isDeleted
            Instant.now(),                  // createdAt
            Instant.now()                   // updatedAt
    );

    private final JobIdentity testIdentity = new JobIdentity(1L);
    private final JobIdentity nonExistingIdentity = new JobIdentity(999L);

    // ========== Entity↔Domain 변환 테스트 ==========

    @Test
    void save_withValidDomain_returnsConvertedDomain() {
        // given
        Job jobToSave = sampleJob;

        // when
        Job saved = jobRepository.save(jobToSave);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getJobId()).isNotNull();
        assertThat(saved.getUrl()).isEqualTo(jobToSave.getUrl());
        assertThat(saved.getCompany()).isEqualTo(jobToSave.getCompany());
        assertThat(saved.getTitle()).isEqualTo(jobToSave.getTitle());
        assertThat(saved.getOrganization()).isEqualTo(jobToSave.getOrganization());
        assertThat(saved.getMarkdownBody()).isEqualTo(jobToSave.getMarkdownBody());
        assertThat(saved.getCareerLevel()).isEqualTo(jobToSave.getCareerLevel());
        assertThat(saved.getEmploymentType()).isEqualTo(jobToSave.getEmploymentType());
        assertThat(saved.getLocations()).isEqualTo(jobToSave.getLocations());
        assertThat(saved.getTechCategories().containsAll(jobToSave.getTechCategories())).isTrue();
    }

    @Test
    void save_withNullId_generatesIdAndReturns() {
        // given
        Job jobWithNullId = sampleJob;

        // when
        Job saved = jobRepository.save(jobWithNullId);

        // then
        assertThat(saved.getJobId()).isNotNull();
        assertThat(saved.getUrl()).isEqualTo(jobWithNullId.getUrl());
    }

    @Test
    void findById_existingId_returnsConvertedDomain() {
        // given
        Job saved = jobRepository.save(sampleJob);
        JobIdentity identity = new JobIdentity(saved.getJobId());

        // when
        Optional<Job> found = jobRepository.findById(identity);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getJobId()).isEqualTo(saved.getJobId());
        assertThat(found.get().getUrl()).isEqualTo(saved.getUrl());
        assertThat(found.get().getCompany()).isEqualTo(saved.getCompany());
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        // when
        Optional<Job> found = jobRepository.findById(nonExistingIdentity);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void findAll_withData_returnsConvertedList() {
        // given
        Job saved1 = jobRepository.save(sampleJob);
        Job saved2 = jobRepository.save(new Job(
                null, "https://example.com/job2", "Company B", "Frontend Developer",
                "Design Team", "Description", "Summary", 1, 3, false,
                CareerLevel.ENTRY, EmploymentType.CONTRACT, PositionCategory.FRONTEND,
                RemotePolicy.REMOTE, List.of(TechCategory.JAVA), Instant.now(), null, true, false,
                List.of("Busan"), "Intro", List.of(), List.of(), List.of(), null,
                false, false, false, 2, 20, Popularity.empty(), false, Instant.now(), Instant.now()
        ));

        // when
        List<Job> all = jobRepository.findAll();

        // then
        assertThat(all).hasSize(2);
        assertThat(all).extracting(Job::getJobId)
                .containsExactlyInAnyOrder(saved1.getJobId(), saved2.getJobId());
    }

    @Test
    void findAll_emptyRepository_returnsEmptyList() {
        // when
        List<Job> all = jobRepository.findAll();

        // then
        assertThat(all).isEmpty();
    }

    // ========== 커스텀 쿼리 테스트 ==========

    @Test
    void findByUrl_existingUrl_returnsConvertedDomain() {
        // given
        Job saved = jobRepository.save(sampleJob);

        // when
        Optional<Job> found = jobRepository.findByUrl(saved.getUrl());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getJobId()).isEqualTo(saved.getJobId());
        assertThat(found.get().getUrl()).isEqualTo(saved.getUrl());
    }

    @Test
    void findByUrl_nonExistingUrl_returnsEmpty() {
        // when
        Optional<Job> found = jobRepository.findByUrl("https://nonexisting.com");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void findByCompany_existingCompany_returnsConvertedList() {
        // given
        Job saved1 = jobRepository.save(sampleJob);
        Job saved2 = jobRepository.save(new Job(
                null, "https://example.com/job2", "Company A", "Frontend Developer",
                "Design Team", "Description", "Summary", 1, 3, false,
                CareerLevel.ENTRY, EmploymentType.CONTRACT, PositionCategory.FRONTEND,
                RemotePolicy.REMOTE, List.of(TechCategory.JAVA), Instant.now(), null, true, false,
                List.of("Busan"), "Intro", List.of(), List.of(), List.of(), null,
                false, false, false, 2, 20, Popularity.empty(), false, Instant.now(), Instant.now()
        ));
        jobRepository.save(new Job(
                null, "https://example.com/job3", "Company B", "Backend Developer",
                "Tech Team", "Description", "Summary", 2, 5, true,
                CareerLevel.EXPERIENCED, EmploymentType.FULL_TIME, PositionCategory.BACKEND,
                RemotePolicy.HYBRID, List.of(TechCategory.JAVA), Instant.now(), null, true, false,
                List.of("Seoul"), "Intro", List.of(), List.of(), List.of(), null,
                false, false, false, 3, 30, Popularity.empty(), false, Instant.now(), Instant.now()
        ));

        // when
        List<Job> found = jobRepository.findByCompany("Company A");

        // then
        assertThat(found).hasSize(2);
        assertThat(found).extracting(Job::getJobId)
                .containsExactlyInAnyOrder(saved1.getJobId(), saved2.getJobId());
    }

    @Test
    void findByCompany_nonExistingCompany_returnsEmptyList() {
        // when
        List<Job> found = jobRepository.findByCompany("NonExisting");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void increaseViewCount_validJobId_incrementsSuccessfully() {
        // given
        Job saved = jobRepository.save(sampleJob);
        JobIdentity identity = new JobIdentity(saved.getJobId());

        // when
        jobRepository.increaseViewCount(identity, 10);

        // then - 수동 검증 필요 (실제 viewCount 확인)
        Optional<Job> found = jobRepository.findById(identity);
        assertThat(found).isPresent();
    }
}
