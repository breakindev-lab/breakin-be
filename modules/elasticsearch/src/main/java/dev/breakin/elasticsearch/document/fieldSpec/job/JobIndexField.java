package dev.breakin.elasticsearch.document.fieldSpec.job;


import dev.breakin.elasticsearch.document.fieldSpec.FieldName;
import dev.breakin.elasticsearch.document.fieldSpec.QueryType;

public enum JobIndexField implements FieldName {
    DOC_ID("doc_id", QueryType.TERM),
    JOB_ID("job_id", QueryType.TERM),
    URL("url", QueryType.TERM),

    COMPANY("company", QueryType.TERM),
    TITLE("title", QueryType.MATCH),
    ORGANIZATION("organization", QueryType.TERM),
    LOCATION("location", QueryType.TERM),

    EMPLOYMENT_TYPE("employment_type", QueryType.TERM),
    CAREER_LEVEL("career_level", QueryType.TERM),
    POSITION_CATEGORY("position_category", QueryType.TERM),
    REMOTE_POLICY("remote_policy", QueryType.TERM),
    TECH_CATEGORIES("tech_categories", QueryType.TERM),

    MIN_YEARS("min_years", QueryType.RANGE),
    MAX_YEARS("max_years", QueryType.RANGE),
    EXPERIENCE_REQUIRED("experience_required", QueryType.TERM),

    STARTED_AT("started_at", QueryType.RANGE),
    ENDED_AT("ended_at", QueryType.RANGE),
    IS_OPEN_ENDED("is_open_ended", QueryType.TERM),
    IS_CLOSED("is_closed", QueryType.TERM),

    HAS_ASSIGNMENT("has_assignment", QueryType.TERM),
    HAS_CODING_TEST("has_coding_test", QueryType.TERM),
    HAS_LIVE_CODING("has_live_coding", QueryType.TERM),

    INTERVIEW_COUNT("interview_count", QueryType.RANGE),
    INTERVIEW_DAYS("interview_days", QueryType.RANGE),

    MARKDOWN_BODY("markdown_body", QueryType.MATCH),
    ONE_LINE_SUMMARY("one_line_summary", QueryType.MATCH),

    POPULARITY("popularity", QueryType.NESTED), // nested 루트
    POPULARITY_VIEW_COUNT("popularity.view_count", QueryType.RANGE),
    POPULARITY_COMMENT_COUNT("popularity.comment_count", QueryType.RANGE),
    POPULARITY_LIKE_COUNT("popularity.like_count", QueryType.RANGE),

    CREATED_AT("created_at", QueryType.RANGE),
    UPDATED_AT("updated_at", QueryType.RANGE),
    DELETED("deleted", QueryType.TERM);

    private final String fieldName;
    private final QueryType queryType;

    JobIndexField(String fieldName, QueryType queryType) {
        this.fieldName = fieldName;
        this.queryType = queryType;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    public QueryType getQueryType() {
        return queryType;
    }
}