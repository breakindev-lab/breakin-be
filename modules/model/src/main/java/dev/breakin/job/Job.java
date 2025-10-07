package dev.breakin.job;

import dev.breakin.common.Popularity;
import lombok.Value;
import java.time.Instant;
import java.util.List;

@Value
public class Job implements JobModel {
    Long jobId;
    String url;
    String company;
    String title;
    String organization;
    String markdownBody;
    String oneLineSummary;
    Integer minYears;
    Integer maxYears;
    Boolean experienceRequired;
    String careerLevel;
    String employmentType;
    String positionCategory;
    String remotePolicy;
    List<String> techCategories;
    Instant startedAt;
    Instant endedAt;
    Boolean isOpenEnded;
    Boolean isClosed;
    String location;
    Boolean hasAssignment;
    Boolean hasCodingTest;
    Boolean hasLiveCoding;
    Integer interviewCount;
    Integer interviewDays;
    Popularity popularity;
    Boolean isDeleted;
    Instant createdAt;
    Instant updatedAt;

    public static Job newJob(
        String url,
        String company,
        String title,
        String organization,
        String markdownBody,
        String location
    ) {
        Instant now = Instant.now();
        return new Job(
            null,
            url,
            company,
            title,
            organization,
            markdownBody,
            null,
            null,
            null,
            false,
            "ENTRY",
            "FULL_TIME",
            null,
            "ONSITE",
            List.of(),
            now,
            null,
            false,
            false,
            location,
            false,
            false,
            false,
            null,
            null,
            Popularity.empty(),
            false,
            now,
            now
        );
    }

    public Job close() {
        return new Job(
            jobId, url, company, title, organization, markdownBody, oneLineSummary,
            minYears, maxYears, experienceRequired, careerLevel, employmentType,
            positionCategory, remotePolicy, techCategories, startedAt, endedAt,
            isOpenEnded, true, location, hasAssignment, hasCodingTest, hasLiveCoding,
            interviewCount, interviewDays, popularity, isDeleted, createdAt, Instant.now()
        );
    }

    public Job incrementViewCount() {
        return new Job(
            jobId, url, company, title, organization, markdownBody, oneLineSummary,
            minYears, maxYears, experienceRequired, careerLevel, employmentType,
            positionCategory, remotePolicy, techCategories, startedAt, endedAt,
            isOpenEnded, isClosed, location, hasAssignment, hasCodingTest, hasLiveCoding,
            interviewCount, interviewDays, popularity.incrementViewCount(), isDeleted,
            createdAt, Instant.now()
        );
    }

    public Job incrementCommentCount() {
        return new Job(
            jobId, url, company, title, organization, markdownBody, oneLineSummary,
            minYears, maxYears, experienceRequired, careerLevel, employmentType,
            positionCategory, remotePolicy, techCategories, startedAt, endedAt,
            isOpenEnded, isClosed, location, hasAssignment, hasCodingTest, hasLiveCoding,
            interviewCount, interviewDays, popularity.incrementCommentCount(), isDeleted,
            createdAt, Instant.now()
        );
    }

    public Job incrementLikeCount() {
        return new Job(
            jobId, url, company, title, organization, markdownBody, oneLineSummary,
            minYears, maxYears, experienceRequired, careerLevel, employmentType,
            positionCategory, remotePolicy, techCategories, startedAt, endedAt,
            isOpenEnded, isClosed, location, hasAssignment, hasCodingTest, hasLiveCoding,
            interviewCount, interviewDays, popularity.incrementLikeCount(), isDeleted,
            createdAt, Instant.now()
        );
    }

    public Job decrementLikeCount() {
        return new Job(
            jobId, url, company, title, organization, markdownBody, oneLineSummary,
            minYears, maxYears, experienceRequired, careerLevel, employmentType,
            positionCategory, remotePolicy, techCategories, startedAt, endedAt,
            isOpenEnded, isClosed, location, hasAssignment, hasCodingTest, hasLiveCoding,
            interviewCount, interviewDays, popularity.decrementLikeCount(), isDeleted,
            createdAt, Instant.now()
        );
    }

    public Job markAsDeleted() {
        return new Job(
            jobId, url, company, title, organization, markdownBody, oneLineSummary,
            minYears, maxYears, experienceRequired, careerLevel, employmentType,
            positionCategory, remotePolicy, techCategories, startedAt, endedAt,
            isOpenEnded, isClosed, location, hasAssignment, hasCodingTest, hasLiveCoding,
            interviewCount, interviewDays, popularity, true, createdAt, Instant.now()
        );
    }
}
