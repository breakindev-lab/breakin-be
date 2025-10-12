package dev.breakin.search.job;

import dev.breakin.elasticsearch.api.job.JobSearch;
import dev.breakin.elasticsearch.api.job.JobSearchResult;
import dev.breakin.elasticsearch.document.fieldSpec.job.JobIndexField;
import dev.breakin.elasticsearch.internal.queryBuilder.SearchCommand;
import dev.breakin.elasticsearch.internal.queryBuilder.SearchElement;
import dev.breakin.search.job.dto.JobSearchRequest;
import dev.breakin.search.job.dto.JobSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Job Search API Controller
 */
@RestController
@RequestMapping("/api/jobs/search")
@RequiredArgsConstructor
@Slf4j
public class JobSearchController {

    private final JobSearch jobSearch;

    /**
     * Search jobs with filters
     *
     * @param request search conditions and filters
     * @return search results with pagination
     */
    @PostMapping
    public ResponseEntity<JobSearchResponse> search(@RequestBody JobSearchRequest request) {
        log.info("Job search request: {}", request);

        // Build SearchCommand
        List<SearchElement<JobIndexField>> conditions = buildSearchConditions(request);
        SearchCommand<JobIndexField> command = SearchCommand.of(
                conditions,
                request.getFrom(),
                request.getTo()
        );

        // Execute search
        JobSearchResult result = jobSearch.search(command);

        // Convert response (JobDoc -> JobCard)
        JobSearchResponse response = JobSearchResponse.from(result);

        log.info("Job search completed: found {} results", result.docs().size());
        return ResponseEntity.ok(response);
    }

    /**
     * Build search conditions from request
     */
    private List<SearchElement<JobIndexField>> buildSearchConditions(JobSearchRequest request) {
        List<SearchElement<JobIndexField>> conditions = new ArrayList<>();

        // Title search
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            conditions.add(new SearchElement<>(JobIndexField.TITLE, request.getTitle()));
        }

        // Company filter
        if (request.getCompany() != null) {
            conditions.add(new SearchElement<>(JobIndexField.COMPANY, request.getCompany()));
        }

        // Career level filter
        if (request.getCareerLevel() != null) {
            conditions.add(new SearchElement<>(JobIndexField.CAREER_LEVEL, request.getCareerLevel()));
        }

        // Employment type filter
        if (request.getEmploymentType() != null) {
            conditions.add(new SearchElement<>(JobIndexField.EMPLOYMENT_TYPE, request.getEmploymentType()));
        }

        // Position category filter
        if (request.getPositionCategory() != null) {
            conditions.add(new SearchElement<>(JobIndexField.POSITION_CATEGORY, request.getPositionCategory()));
        }

        // Remote policy filter
        if (request.getRemotePolicy() != null) {
            conditions.add(new SearchElement<>(JobIndexField.REMOTE_POLICY, request.getRemotePolicy()));
        }

        // Tech categories filter
        if (request.getTechCategories() != null && !request.getTechCategories().isEmpty()) {
            for (String tech : request.getTechCategories()) {
                conditions.add(new SearchElement<>(JobIndexField.TECH_CATEGORIES, tech));
            }
        }

        // Location filter
        if (request.getLocation() != null && !request.getLocation().isBlank()) {
            conditions.add(new SearchElement<>(JobIndexField.LOCATIONS, request.getLocation()));
        }

        // Experience years range filter
        if (request.getMinYears() != null) {
            conditions.add(new SearchElement<>(JobIndexField.MIN_YEARS, request.getMinYears(), null));
        }
        if (request.getMaxYears() != null) {
            conditions.add(new SearchElement<>(JobIndexField.MAX_YEARS, null, request.getMaxYears()));
        }

        // Deleted filter (default: exclude deleted jobs)
        conditions.add(new SearchElement<>(JobIndexField.DELETED, false));

        return conditions;
    }
}
