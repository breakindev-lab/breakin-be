package dev.breakin.api.job.dto;

import dev.breakin.model.common.Company;
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

        // Company enum 변환
        Company companyEnum = Company.valueOf(company.toUpperCase());

        // 중첩 객체 생성
        ExperienceRequirement experience = ExperienceRequirement.of(
            minYears,
            maxYears,
            experienceRequired != null ? experienceRequired : false,
            careerLevel != null ? careerLevel : CareerLevel.ENTRY
        );

        JobDescription description = JobDescription.of(
            null,  // positionIntroduction
            List.of(),  // responsibilities
            List.of(),  // qualifications
            List.of(),  // preferredQualifications
            markdownBody  // fullDescription
        );

        InterviewProcess interview = InterviewProcess.of(
            hasAssignment != null ? hasAssignment : false,
            hasCodingTest != null ? hasCodingTest : false,
            hasLiveCoding != null ? hasLiveCoding : false,
            interviewCount,
            interviewDays
        );

        return new Job(
                jobId,
                url,
                companyEnum,
                title,
                organization,
                oneLineSummary,
                experience,
                employmentType != null ? employmentType : EmploymentType.FULL_TIME,
                positionCategory,
                remotePolicy != null ? remotePolicy : RemotePolicy.ONSITE,
                techCategories != null ? techCategories : List.of(),
                startedAt != null ? startedAt : now,
                endedAt,
                isOpenEnded != null ? isOpenEnded : false,
                isClosed != null ? isClosed : false,
                location != null ? List.of(location) : List.of(),  // String → List<String>
                description,
                interview,
                JobCompensation.empty(),
                Popularity.empty(),
                false,
                now,
                now
        );
    }
}
