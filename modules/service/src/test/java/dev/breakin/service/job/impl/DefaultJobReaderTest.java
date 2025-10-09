package dev.breakin.service.job.impl;

import dev.breakin.infra.job.repository.JobRepository;
import dev.breakin.model.common.Popularity;
import dev.breakin.model.common.TechCategory;
import dev.breakin.model.job.*;
import dev.breakin.service.job.view.JobViewMemory;
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
class DefaultJobReaderTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private JobViewMemory jobViewMemory;

    @InjectMocks
    private DefaultJobReader jobReader;

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

    private final JobIdentity testIdentity = new JobIdentity(1L);

    @Test
    void read_existingId_returnsJobAndIncrementsViewCount() {
        // given
        when(jobRepository.findById(testIdentity))
            .thenReturn(Optional.of(sampleJob));

        // when
        Job result = jobReader.read(testIdentity);

        // then
        assertNotNull(result);
        assertEquals(sampleJob.getJobId(), result.getJobId());
        assertEquals(sampleJob.getTitle(), result.getTitle());
        verify(jobRepository).findById(testIdentity);
        verify(jobViewMemory).countUp(1L);
    }

    @Test
    void read_nonExistingId_throwsException() {
        // given
        when(jobRepository.findById(testIdentity))
            .thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () ->
            jobReader.read(testIdentity)
        );
        verify(jobRepository).findById(testIdentity);
        verify(jobViewMemory, never()).countUp(any());
    }

    @Test
    void getById_existingId_returnsJobWithoutViewCount() {
        // given
        when(jobRepository.findById(testIdentity))
            .thenReturn(Optional.of(sampleJob));

        // when
        Job result = jobReader.getById(testIdentity);

        // then
        assertNotNull(result);
        assertEquals(sampleJob.getJobId(), result.getJobId());
        assertEquals(sampleJob.getTitle(), result.getTitle());
        verify(jobRepository).findById(testIdentity);
        verify(jobViewMemory, never()).countUp(any());
    }

    @Test
    void getById_nonExistingId_throwsException() {
        // given
        when(jobRepository.findById(testIdentity))
            .thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () ->
            jobReader.getById(testIdentity)
        );
        verify(jobRepository).findById(testIdentity);
    }

    @Test
    void getAll_withData_returnsList() {
        // given
        List<Job> jobs = List.of(sampleJob);
        when(jobRepository.findAll())
            .thenReturn(jobs);

        // when
        List<Job> result = jobReader.getAll();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleJob.getJobId(), result.get(0).getJobId());
        verify(jobRepository).findAll();
    }

    @Test
    void getAll_emptyData_returnsEmptyList() {
        // given
        when(jobRepository.findAll())
            .thenReturn(List.of());

        // when
        List<Job> result = jobReader.getAll();

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jobRepository).findAll();
    }

    @Test
    void getByUrl_existingUrl_returnsJob() {
        // given
        String url = "https://example.com/job/1";
        when(jobRepository.findByUrl(url))
            .thenReturn(Optional.of(sampleJob));

        // when
        Job result = jobReader.getByUrl(url);

        // then
        assertNotNull(result);
        assertEquals(url, result.getUrl());
        verify(jobRepository).findByUrl(url);
    }

    @Test
    void getByUrl_nonExistingUrl_throwsException() {
        // given
        String url = "https://example.com/nonexistent";
        when(jobRepository.findByUrl(url))
            .thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () ->
            jobReader.getByUrl(url)
        );
        verify(jobRepository).findByUrl(url);
    }

    @Test
    void getByCompany_existingCompany_returnsList() {
        // given
        String company = "test company";
        List<Job> jobs = List.of(sampleJob);
        when(jobRepository.findByCompany(company))
            .thenReturn(jobs);

        // when
        List<Job> result = jobReader.getByCompany(company);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(company, result.get(0).getCompany());
        verify(jobRepository).findByCompany(company);
    }

    @Test
    void getByCompany_nonExistingCompany_returnsEmptyList() {
        // given
        String company = "nonexistent company";
        when(jobRepository.findByCompany(company))
            .thenReturn(List.of());

        // when
        List<Job> result = jobReader.getByCompany(company);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jobRepository).findByCompany(company);
    }
}
