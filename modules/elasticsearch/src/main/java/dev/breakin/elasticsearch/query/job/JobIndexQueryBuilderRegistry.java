package dev.breakin.elasticsearch.query.job;


import dev.breakin.elasticsearch.document.fieldSpec.job.JobIndexField;
import dev.breakin.elasticsearch.queryBuilder.BoolType;
import dev.breakin.elasticsearch.queryBuilder.FieldQueryBuilder;
import dev.breakin.elasticsearch.queryBuilder.queryBuilder.MatchQueryBuilder;
import dev.breakin.elasticsearch.queryBuilder.queryBuilder.TermQueryBuilder;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class JobIndexQueryBuilderRegistry {
    private static final Map<JobIndexField, FieldQueryBuilder> builderMap =
            new EnumMap<>(JobIndexField.class);

    static {
        register(JobIndexField.DOC_ID, new TermQueryBuilder(BoolType.FILTER));
        register(JobIndexField.JOB_ID, new TermQueryBuilder(BoolType.FILTER));
        register(JobIndexField.URL, new TermQueryBuilder(BoolType.FILTER));

        register(JobIndexField.COMPANY, new TermQueryBuilder(BoolType.FILTER));
        register(JobIndexField.TITLE, new MatchQueryBuilder(BoolType.SHOULD));
        register(JobIndexField.ORGANIZATION, new TermQueryBuilder(BoolType.FILTER));
        register(JobIndexField.LOCATION, new TermQueryBuilder(BoolType.FILTER));

        register(JobIndexField.EMPLOYMENT_TYPE, new TermQueryBuilder(BoolType.FILTER));
        register(JobIndexField.CAREER_LEVEL, new TermQueryBuilder(BoolType.FILTER));
        register(JobIndexField.POSITION_CATEGORY, new TermQueryBuilder(BoolType.FILTER));
        register(JobIndexField.REMOTE_POLICY, new TermQueryBuilder(BoolType.FILTER));
        register(JobIndexField.TECH_CATEGORIES, new TermQueryBuilder(BoolType.FILTER));

        register(JobIndexField.EXPERIENCE_REQUIRED, new TermQueryBuilder(BoolType.FILTER));
        register(JobIndexField.IS_OPEN_ENDED, new TermQueryBuilder(BoolType.FILTER));
        register(JobIndexField.IS_CLOSED, new TermQueryBuilder(BoolType.FILTER));
        register(JobIndexField.HAS_ASSIGNMENT, new TermQueryBuilder(BoolType.FILTER));
        register(JobIndexField.HAS_CODING_TEST, new TermQueryBuilder(BoolType.FILTER));
        register(JobIndexField.HAS_LIVE_CODING, new TermQueryBuilder(BoolType.FILTER));

        register(JobIndexField.MARKDOWN_BODY, new MatchQueryBuilder(BoolType.SHOULD));
        register(JobIndexField.ONE_LINE_SUMMARY, new MatchQueryBuilder(BoolType.SHOULD));
        register(JobIndexField.DELETED, new TermQueryBuilder(BoolType.MUST));

        // Range / Nested는 별도 처리:
        // MIN_YEARS, MAX_YEARS, STARTED_AT, ENDED_AT, INTERVIEW_COUNT, INTERVIEW_DAYS,
        // CREATED_AT, UPDATED_AT → RangeQueryBuilder
        // POPULARITY / POPULARITY_* → Nested / Range 혼합 처리
    }

    private static void register(JobIndexField field, FieldQueryBuilder builder) {
        builderMap.put(field, builder);
    }

    public static Optional<FieldQueryBuilder> getBuilder(JobIndexField field) {
        return Optional.ofNullable(builderMap.get(field));
    }

    public static final Function<JobIndexField, Optional<FieldQueryBuilder>> LOOKUP =
            JobIndexQueryBuilderRegistry::getBuilder;
}
