package dev.breakin.service.job.impl;

import dev.breakin.infra.job.repository.JobRepository;
import dev.breakin.model.common.Popularity;
import dev.breakin.model.common.TechCategory;
import dev.breakin.model.job.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultJobWriterTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private DefaultJobWriter jobWriter;

    private final Job sampleJob = new Job(
        1L,                              // jobId
        "https://example.com/job/1",     // url
        "test company",                  // company
        "test title",                    // title
        "test organization",             // organization
        "test markdown body",            // markdownBody
        "test one line summary",         // oneLineSummary
        0,                               // minYears
        3,                               // maxYears
        false,                           // experienceRequired
        CareerLevel.ENTRY,               // careerLevel
        EmploymentType.FULL_TIME,        // employmentType
        PositionCategory.ENGINEERING,    // positionCategory
        RemotePolicy.ONSITE,             // remotePolicy
        List.of(TechCategory.JAVA),      // techCategories
        Instant.now(),                   // startedAt
        null,                            // endedAt
        false,                           // isOpenEnded
        false,                           // isClosed
        "Seoul",                         // location
        false,                           // hasAssignment
        false,                           // hasCodingTest
        false,                           // hasLiveCoding
        3,                               // interviewCount
        7,                               // interviewDays
        Popularity.empty(),              // popularity
        false,                           // isDeleted
        Instant.now(),                   // createdAt
        Instant.now()                    // updatedAt
    );

    @Test
    void upsert_newJob_callsRepositorySave() {
        // given
        Job newJob = Job.newJob(
            "https://example.com/job/new",
            "new company",
            "new title",
            "new organization",
            "new markdown body",
            "Seoul"
        );
        when(jobRepository.save(any(Job.class)))
            .thenReturn(sampleJob);

        // when
        Job result = jobWriter.upsert(newJob);

        // then
        assertNotNull(result);
        assertEquals(sampleJob.getJobId(), result.getJobId());
        verify(jobRepository).save(newJob);
    }

    @Test
    void upsert_existingJob_callsRepositorySave() {
        // given
        Job updatedJob = new Job(
            1L, "https://example.com/job/1", "updated company", "updated title",
            "test organization", "updated body", "updated summary",
            0, 3, false, CareerLevel.ENTRY, EmploymentType.FULL_TIME,
            PositionCategory.ENGINEERING, RemotePolicy.ONSITE, List.of(TechCategory.JAVA),
            Instant.now(), null, false, false, "Seoul",
            false, false, false, 3, 7,
            Popularity.empty(), false, Instant.now(), Instant.now()
        );
        when(jobRepository.save(any(Job.class)))
            .thenReturn(updatedJob);

        // when
        Job result = jobWriter.upsert(updatedJob);

        // then
        assertNotNull(result);
        assertEquals("updated company", result.getCompany());
        assertEquals("updated title", result.getTitle());
        verify(jobRepository).save(updatedJob);
    }

    @Test
    void upsert_jobWithTechCategories_callsRepositorySave() {
        // given
        Job jobWithTech = new Job(
            null, "https://example.com/job/tech", "tech company", "tech title",
            "tech org", "tech body", null, 0, 5, false,
            CareerLevel.EXPERIENCED, EmploymentType.FULL_TIME, PositionCategory.ENGINEERING,
            RemotePolicy.ONSITE, List.of(TechCategory.JAVA, TechCategory.MYSQL),
            Instant.now(), null, false, false, "Seoul",
            true, true, false, 2, 5,
            Popularity.empty(), false, Instant.now(), Instant.now()
        );
        when(jobRepository.save(any(Job.class)))
            .thenReturn(jobWithTech);

        // when
        Job result = jobWriter.upsert(jobWithTech);

        // then
        assertNotNull(result);
        assertEquals(2, result.getTechCategories().size());
        assertTrue(result.getTechCategories().contains(TechCategory.JAVA));
        assertTrue(result.getTechCategories().contains(TechCategory.MYSQL));
        verify(jobRepository).save(jobWithTech);
    }

    @Test
    void delete_existingJob_callsRepositoryDelete() {
        // given
        JobIdentity identity = new JobIdentity(1L);
        doNothing().when(jobRepository).deleteById(identity);

        // when
        jobWriter.delete(identity);

        // then
        verify(jobRepository).deleteById(identity);
    }

    @Test
    void delete_callsRepositoryDeleteWithCorrectIdentity() {
        // given
        JobIdentity identity = new JobIdentity(999L);
        doNothing().when(jobRepository).deleteById(identity);

        // when
        jobWriter.delete(identity);

        // then
        verify(jobRepository).deleteById(identity);
    }
}
