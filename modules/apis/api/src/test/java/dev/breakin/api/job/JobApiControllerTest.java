package dev.breakin.api.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.breakin.api.job.dto.JobResponse;
import dev.breakin.model.common.Company;
import dev.breakin.model.common.Popularity;
import dev.breakin.model.common.TechCategory;
import dev.breakin.model.job.*;
import dev.breakin.service.job.JobReader;
import dev.breakin.service.job.JobWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class JobApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JobReader jobReader;

    @Mock
    private JobWriter jobWriter;

    @InjectMocks
    private JobApiController jobApiController;

    private ObjectMapper objectMapper;

    private final Job sampleJob = new Job(
            1L,                                  // jobId
            "https://test.com/job/1",           // url
            Company.GOOGLE,                     // company
            "Senior Backend Developer",         // title
            "Engineering Team",                 // organization
            "Great opportunity",                // oneLineSummary
            ExperienceRequirement.of(3, 7, true, CareerLevel.EXPERIENCED), // experience
            EmploymentType.FULL_TIME,           // employmentType
            PositionCategory.BACKEND,           // positionCategory
            RemotePolicy.HYBRID,                // remotePolicy
            List.of(TechCategory.JAVA, TechCategory.MYSQL), // techCategories
            Instant.now(),                      // startedAt
            null,                               // endedAt
            false,                              // isOpenEnded
            false,                              // isClosed
            List.of("Seoul"),                   // locations
            JobDescription.of(null, List.of(), List.of(), List.of(), "Test markdown body"), // description
            InterviewProcess.of(true, true, false, 3, 14), // interviewProcess
            JobCompensation.empty(),            // compensation
            Popularity.empty(),                 // popularity
            false,                              // isDeleted
            Instant.now(),                      // createdAt
            Instant.now()                       // updatedAt
    );

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(jobApiController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // JavaTimeModule 등록
    }

    @Test
    void getJob_existingId_returnsOkWithJob() throws Exception {
        // given
        when(jobReader.read(new JobIdentity(1L)))
                .thenReturn(sampleJob);

        // when & then - Status Code 검증
        MvcResult result = mockMvc.perform(get("/api/jobs/{jobId}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        // then - Response Spec 검증
        String responseJson = result.getResponse().getContentAsString();
        JobResponse response = objectMapper.readValue(responseJson, JobResponse.class);

        assertThat(response.getJobId()).isEqualTo(1L);
        assertThat(response.getCompany()).isEqualTo("GOOGLE");
        assertThat(response.getTitle()).isEqualTo("Senior Backend Developer");
        assertThat(response.getCareerLevel()).isEqualTo(CareerLevel.EXPERIENCED);
        assertThat(response.getEmploymentType()).isEqualTo(EmploymentType.FULL_TIME);
        assertThat(response.getLocation()).isEqualTo("Seoul");

        verify(jobReader).read(new JobIdentity(1L));
    }

    @Test
    void getJob_nonExistingId_throwsException() throws Exception {
        // given
        when(jobReader.read(new JobIdentity(999L)))
                .thenThrow(new RuntimeException("Job not found"));

        // when & then
        try {
            mockMvc.perform(get("/api/jobs/{jobId}", 999L))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            // Controller에서 RuntimeException 발생 예상
            assertThat(e.getCause()).isInstanceOf(RuntimeException.class);
        }

        verify(jobReader).read(new JobIdentity(999L));
    }

    @Test
    void upsertJob_newJob_returnsCreatedWithJob() throws Exception {
        // given
        when(jobWriter.upsert(any(Job.class)))
                .thenReturn(sampleJob);

        // when & then - Status Code 검증
        MvcResult result = mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"https://test.com/job/1\",\"company\":\"GOOGLE\",\"title\":\"Senior Backend Developer\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        // then - Response Spec 검증
        String responseJson = result.getResponse().getContentAsString();
        JobResponse response = objectMapper.readValue(responseJson, JobResponse.class);

        assertThat(response.getJobId()).isEqualTo(1L);
        assertThat(response.getCompany()).isEqualTo(Company.GOOGLE.name());
        assertThat(response.getTitle()).isEqualTo("Senior Backend Developer");

        verify(jobWriter).upsert(any(Job.class));
    }

    @Test
    void upsertJob_existingJob_returnsCreatedWithUpdatedJob() throws Exception {
        // given
        Job updatedJob = new Job(
                1L,
                "https://test.com/job/1",
                Company.GOOGLE,
                "Updated Title",
                "Engineering Team",
                "Great opportunity",
                ExperienceRequirement.of(3, 7, true, CareerLevel.EXPERIENCED),
                EmploymentType.FULL_TIME,
                PositionCategory.BACKEND,
                RemotePolicy.HYBRID,
                List.of(TechCategory.JAVA),
                Instant.now(),
                null,
                false,
                false,
                List.of("Seoul"),
                JobDescription.of(null, List.of(), List.of(), List.of(), "Updated body"),
                InterviewProcess.of(true, true, false, 3, 14),
                JobCompensation.empty(),
                Popularity.empty(),
                false,
                Instant.now(),
                Instant.now()
        );

        when(jobWriter.upsert(any(Job.class)))
                .thenReturn(updatedJob);

        // when & then
        MvcResult result = mockMvc.perform(post("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"jobId\":1,\"url\":\"https://test.com/job/1\",\"company\":\"META\",\"title\":\"Updated Title\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        JobResponse response = objectMapper.readValue(responseJson, JobResponse.class);

        assertThat(response.getJobId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Updated Title");

        verify(jobWriter).upsert(any(Job.class));
    }

    @Test
    void deleteJob_existingId_returnsNoContent() throws Exception {
        // given
        doNothing().when(jobWriter).delete(new JobIdentity(1L));

        // when & then
        mockMvc.perform(delete("/api/jobs/{jobId}", 1L))
                .andExpect(status().isNoContent());

        verify(jobWriter).delete(new JobIdentity(1L));
    }

    @Test
    void deleteJob_nonExistingId_throwsException() throws Exception {
        // given
        doThrow(new RuntimeException("Job not found"))
                .when(jobWriter).delete(new JobIdentity(999L));

        // when & then
        try {
            mockMvc.perform(delete("/api/jobs/{jobId}", 999L))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(RuntimeException.class);
        }

        verify(jobWriter).delete(new JobIdentity(999L));
    }
}
