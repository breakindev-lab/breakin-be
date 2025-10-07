package dev.breakin.elasticsearch.document;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.breakin.model.job.CareerLevel;
import dev.breakin.model.job.EmploymentType;
import dev.breakin.model.job.PositionCategory;
import dev.breakin.model.job.RemotePolicy;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobDoc implements DocBase {

    @JsonProperty("doc_id")
    String docId;

    @JsonProperty("job_id")
    Long jobId;

    @JsonProperty("url")
    String url;

    @JsonProperty("company")
    String company;

    @JsonProperty("title")
    String title;

    @JsonProperty("organization")
    String organization;

    @JsonProperty("markdown_body")
    String markdownBody;

    @JsonProperty("one_line_summary")
    String oneLineSummary;

    @JsonProperty("min_years")
    Integer minYears;

    @JsonProperty("max_years")
    Integer maxYears;

    @JsonProperty("experience_required")
    Boolean experienceRequired;

    @JsonProperty("career_level")
    CareerLevel careerLevel;            // enum 그대로

    @JsonProperty("employment_type")
    EmploymentType employmentType;      // enum 그대로

    @JsonProperty("position_category")
    PositionCategory positionCategory;  // enum 그대로

    @JsonProperty("remote_policy")
    RemotePolicy remotePolicy;          // enum 그대로

    @JsonProperty("tech_categories")
    List<String> techCategories;

    @JsonProperty("started_at")
    Instant startedAt;

    @JsonProperty("ended_at")
    Instant endedAt;

    @JsonProperty("is_open_ended")
    Boolean isOpenEnded;

    @JsonProperty("is_closed")
    Boolean isClosed;

    @JsonProperty("location")
    String location;

    @JsonProperty("has_assignment")
    Boolean hasAssignment;

    @JsonProperty("has_coding_test")
    Boolean hasCodingTest;

    @JsonProperty("has_live_coding")
    Boolean hasLiveCoding;

    @JsonProperty("interview_count")
    Integer interviewCount;

    @JsonProperty("interview_days")
    Integer interviewDays;

    @JsonProperty("popularity")
    PopularityDoc popularity;

    @JsonProperty("deleted")
    Boolean deleted;

    @JsonProperty("created_at")
    Instant createdAt;

    @JsonProperty("updated_at")
    Instant updatedAt;

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PopularityDoc {
        @JsonProperty("view_count")    Integer viewCount;
        @JsonProperty("comment_count") Integer commentCount;
        @JsonProperty("like_count")    Integer likeCount;
    }
}