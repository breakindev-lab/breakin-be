package dev.breakin.model.job;

import dev.breakin.model.AuditProps;
import dev.breakin.model.common.Company;
import dev.breakin.model.common.Popularity;
import dev.breakin.model.common.TechCategory;
import lombok.Value;
import java.time.Instant;
import java.util.List;

@Value
public class Job implements AuditProps {
    Long jobId;
    String url;
    Company company;
    String title;
    String organization;
    String markdownBody;
    String oneLineSummary;
    Integer minYears;
    Integer maxYears;
    Boolean experienceRequired;
    CareerLevel careerLevel;
    EmploymentType employmentType;
    PositionCategory positionCategory;
    RemotePolicy remotePolicy;
    List<TechCategory> techCategories;
    Instant startedAt;
    Instant endedAt;
    Boolean isOpenEnded;
    Boolean isClosed;
    List<String> locations;
    String positionIntroduction;
    List<String> responsibilities;
    List<String> qualifications;
    List<String> preferredQualifications;
    String fullDescription;
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
        Company company,
        String title,
        String organization,
        String markdownBody,
        List<String> locations
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
            CareerLevel.ENTRY,
            EmploymentType.FULL_TIME,
            null,
            RemotePolicy.ONSITE,
            List.of(),
            now,
            null,
            false,
            false,
            locations,
            null,
            List.of(),
            List.of(),
            List.of(),
            null,
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
            isOpenEnded, true, locations, positionIntroduction, responsibilities,
            qualifications, preferredQualifications, fullDescription, hasAssignment,
            hasCodingTest, hasLiveCoding, interviewCount, interviewDays, popularity,
            isDeleted, createdAt, Instant.now()
        );
    }

    public Job incrementViewCount() {
        return new Job(
            jobId, url, company, title, organization, markdownBody, oneLineSummary,
            minYears, maxYears, experienceRequired, careerLevel, employmentType,
            positionCategory, remotePolicy, techCategories, startedAt, endedAt,
            isOpenEnded, isClosed, locations, positionIntroduction, responsibilities,
            qualifications, preferredQualifications, fullDescription, hasAssignment,
            hasCodingTest, hasLiveCoding, interviewCount, interviewDays,
            popularity.incrementViewCount(), isDeleted, createdAt, Instant.now()
        );
    }

    public Job incrementViewCount(long adder) {
        return new Job(
                jobId, url, company, title, organization, markdownBody, oneLineSummary,
                minYears, maxYears, experienceRequired, careerLevel, employmentType,
                positionCategory, remotePolicy, techCategories, startedAt, endedAt,
                isOpenEnded, isClosed, locations, positionIntroduction, responsibilities,
                qualifications, preferredQualifications, fullDescription, hasAssignment,
                hasCodingTest, hasLiveCoding, interviewCount, interviewDays,
                popularity.incrementViewCount(adder), isDeleted, createdAt, Instant.now()
        );
    }

    public Job incrementCommentCount() {
        return new Job(
            jobId, url, company, title, organization, markdownBody, oneLineSummary,
            minYears, maxYears, experienceRequired, careerLevel, employmentType,
            positionCategory, remotePolicy, techCategories, startedAt, endedAt,
            isOpenEnded, isClosed, locations, positionIntroduction, responsibilities,
            qualifications, preferredQualifications, fullDescription, hasAssignment,
            hasCodingTest, hasLiveCoding, interviewCount, interviewDays,
            popularity.incrementCommentCount(), isDeleted, createdAt, Instant.now()
        );
    }

    public Job incrementLikeCount() {
        return new Job(
            jobId, url, company, title, organization, markdownBody, oneLineSummary,
            minYears, maxYears, experienceRequired, careerLevel, employmentType,
            positionCategory, remotePolicy, techCategories, startedAt, endedAt,
            isOpenEnded, isClosed, locations, positionIntroduction, responsibilities,
            qualifications, preferredQualifications, fullDescription, hasAssignment,
            hasCodingTest, hasLiveCoding, interviewCount, interviewDays,
            popularity.incrementLikeCount(), isDeleted, createdAt, Instant.now()
        );
    }

    public Job decrementLikeCount() {
        return new Job(
            jobId, url, company, title, organization, markdownBody, oneLineSummary,
            minYears, maxYears, experienceRequired, careerLevel, employmentType,
            positionCategory, remotePolicy, techCategories, startedAt, endedAt,
            isOpenEnded, isClosed, locations, positionIntroduction, responsibilities,
            qualifications, preferredQualifications, fullDescription, hasAssignment,
            hasCodingTest, hasLiveCoding, interviewCount, interviewDays,
            popularity.decrementLikeCount(), isDeleted, createdAt, Instant.now()
        );
    }

    public Job markAsDeleted() {
        return new Job(
            jobId, url, company, title, organization, markdownBody, oneLineSummary,
            minYears, maxYears, experienceRequired, careerLevel, employmentType,
            positionCategory, remotePolicy, techCategories, startedAt, endedAt,
            isOpenEnded, isClosed, locations, positionIntroduction, responsibilities,
            qualifications, preferredQualifications, fullDescription, hasAssignment,
            hasCodingTest, hasLiveCoding, interviewCount, interviewDays, popularity,
            true, createdAt, Instant.now()
        );
    }
}
