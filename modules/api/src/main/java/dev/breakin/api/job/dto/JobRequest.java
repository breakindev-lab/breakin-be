package dev.breakin.api.job.dto;

import dev.breakin.model.common.Popularity;
import dev.breakin.model.common.TechCategory;
import dev.breakin.model.job.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@NoArgsConstructor
public class JobRequest {
    private Long jobId;

    @NotBlank
    private String url;

    @NotBlank
    private String company;

    @NotBlank
    private String title;

    private String organization;
    private String markdownBody;
    private String oneLineSummary;
    private Integer minYears;
    private Integer maxYears;
    private Boolean experienceRequired;
    private CareerLevel careerLevel;
    private EmploymentType employmentType;
    private PositionCategory positionCategory;
    private RemotePolicy remotePolicy;
    private List<TechCategory> techCategories;
    private Instant startedAt;
    private Instant endedAt;
    private Boolean isOpenEnded;
    private Boolean isClosed;
    private String location;
    private Boolean hasAssignment;
    private Boolean hasCodingTest;
    private Boolean hasLiveCoding;
    private Integer interviewCount;
    private Integer interviewDays;

    public Job toJob() {
        Instant now = Instant.now();
        return new Job(
                jobId,
                url,
                company,
                title,
                organization,
                markdownBody,
                oneLineSummary,
                minYears,
                maxYears,
                experienceRequired != null ? experienceRequired : false,
                careerLevel != null ? careerLevel : CareerLevel.ENTRY,
                employmentType != null ? employmentType : EmploymentType.FULL_TIME,
                positionCategory,
                remotePolicy != null ? remotePolicy : RemotePolicy.ONSITE,
                techCategories != null ? techCategories : List.of(),
                startedAt != null ? startedAt : now,
                endedAt,
                isOpenEnded != null ? isOpenEnded : false,
                isClosed != null ? isClosed : false,
                location,
                hasAssignment != null ? hasAssignment : false,
                hasCodingTest != null ? hasCodingTest : false,
                hasLiveCoding != null ? hasLiveCoding : false,
                interviewCount,
                interviewDays,
                Popularity.empty(),
                false,
                now,
                now
        );
    }
}
