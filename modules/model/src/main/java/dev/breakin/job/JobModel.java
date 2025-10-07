package dev.breakin.job;

import dev.breakin.AuditProps;
import dev.breakin.common.Popularity;
import java.time.Instant;
import java.util.List;

public interface JobModel extends AuditProps {
    Long getJobId();
    String getUrl();
    String getCompany();
    String getTitle();
    String getOrganization();
    String getMarkdownBody();
    String getOneLineSummary();
    Integer getMinYears();
    Integer getMaxYears();
    Boolean getExperienceRequired();
    String getCareerLevel();
    String getEmploymentType();
    String getPositionCategory();
    String getRemotePolicy();
    List<String> getTechCategories();
    Instant getStartedAt();
    Instant getEndedAt();
    Boolean getIsOpenEnded();
    Boolean getIsClosed();
    String getLocation();
    Boolean getHasAssignment();
    Boolean getHasCodingTest();
    Boolean getHasLiveCoding();
    Integer getInterviewCount();
    Integer getInterviewDays();
    Popularity getPopularity();
    Boolean getIsDeleted();
}
