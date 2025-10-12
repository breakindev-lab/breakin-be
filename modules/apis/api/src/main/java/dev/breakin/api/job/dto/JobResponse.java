package dev.breakin.api.job.dto;

import dev.breakin.model.common.Popularity;
import dev.breakin.model.common.TechCategory;
import dev.breakin.model.job.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor
public class JobResponse {
    private final Long jobId;
    private final String url;
    private final String company;
    private final String title;
    private final String organization;
    private final String markdownBody;
    private final String oneLineSummary;
    private final Integer minYears;
    private final Integer maxYears;
    private final Boolean experienceRequired;
    private final CareerLevel careerLevel;
    private final EmploymentType employmentType;
    private final PositionCategory positionCategory;
    private final RemotePolicy remotePolicy;
    private final List<TechCategory> techCategories;
    private final Instant startedAt;
    private final Instant endedAt;
    private final Boolean isOpenEnded;
    private final Boolean isClosed;
    private final String location;
    private final Boolean hasAssignment;
    private final Boolean hasCodingTest;
    private final Boolean hasLiveCoding;
    private final Integer interviewCount;
    private final Integer interviewDays;
    private final Popularity popularity;
    private final Boolean isDeleted;
    private final Instant createdAt;
    private final Instant updatedAt;

    public static JobResponse from(Job job) {
        var experience = job.getExperience();
        var description = job.getDescription();
        var interviewProcess = job.getInterviewProcess();

        return new JobResponse(
                job.getJobId(),
                job.getUrl(),
                job.getCompany().name(),
                job.getTitle(),
                job.getOrganization(),
                description != null ? description.getFullDescription() : null,
                job.getOneLineSummary(),
                experience != null ? experience.getMinYears() : null,
                experience != null ? experience.getMaxYears() : null,
                experience != null ? experience.getRequired() : null,
                experience != null ? experience.getCareerLevel() : null,
                job.getEmploymentType(),
                job.getPositionCategory(),
                job.getRemotePolicy(),
                job.getTechCategories(),
                job.getStartedAt(),
                job.getEndedAt(),
                job.getIsOpenEnded(),
                job.getIsClosed(),
                job.getLocations() != null && !job.getLocations().isEmpty()
                    ? job.getLocations().get(0) : null,
                interviewProcess != null ? interviewProcess.getHasAssignment() : null,
                interviewProcess != null ? interviewProcess.getHasCodingTest() : null,
                interviewProcess != null ? interviewProcess.getHasLiveCoding() : null,
                interviewProcess != null ? interviewProcess.getInterviewCount() : null,
                interviewProcess != null ? interviewProcess.getInterviewDays() : null,
                job.getPopularity(),
                job.getIsDeleted(),
                job.getCreatedAt(),
                job.getUpdatedAt()
        );
    }
}
