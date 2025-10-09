package dev.breakin.api.job;

import dev.breakin.api.job.dto.JobRequest;
import dev.breakin.api.job.dto.JobResponse;
import dev.breakin.model.job.Job;
import dev.breakin.model.job.JobIdentity;
import dev.breakin.service.job.JobReader;
import dev.breakin.service.job.JobWriter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Job REST API 컨트롤러
 *
 * Reader/Writer 패턴을 활용하여 CQRS 기반 API를 제공합니다.
 */
@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Job", description = "Job posting API")
public class JobApiController {

    private final JobReader jobReader;
    private final JobWriter jobWriter;

    /**
     * 채용공고 조회
     * GET /api/jobs/{jobId}
     */
    @Operation(summary = "Get job posting", description = "Retrieve a job posting by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Job not found")
    })
    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> getJob(
            @Parameter(description = "Job ID", example = "1") @PathVariable Long jobId) {
        log.info("GET /api/jobs/{}", jobId);

        Job job = jobReader.read(new JobIdentity(jobId));
        JobResponse response = JobResponse.from(job);

        log.info("Retrieved job - jobId: {}", response.getJobId());
        return ResponseEntity.ok(response);
    }

    /**
     * 채용공고 생성/수정
     * POST /api/jobs
     */
    @Operation(summary = "Create or update job posting", description = "Create a new job posting or update existing one")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Successfully created or updated"),
        @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PostMapping
    public ResponseEntity<JobResponse> upsertJob(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Job posting request", required = true)
            @Valid @RequestBody JobRequest request) {
        log.info("POST /api/jobs - company: {}, title: {}",
                request.getCompany(), request.getTitle());

        Job job = request.toJob();
        Job saved = jobWriter.upsert(job);
        JobResponse response = JobResponse.from(saved);

        log.info("Job upserted - jobId: {}", response.getJobId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 채용공고 삭제
     * DELETE /api/jobs/{jobId}
     */
    @Operation(summary = "Delete job posting", description = "Delete a job posting by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Job not found")
    })
    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(
            @Parameter(description = "Job ID", example = "1") @PathVariable Long jobId) {
        log.info("DELETE /api/jobs/{}", jobId);

        jobWriter.delete(new JobIdentity(jobId));

        log.info("Job deleted - jobId: {}", jobId);
        return ResponseEntity.noContent().build();
    }
}
