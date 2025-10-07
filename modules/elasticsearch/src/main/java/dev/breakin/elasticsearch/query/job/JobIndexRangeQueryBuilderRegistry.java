package dev.breakin.elasticsearch.query.job;


import dev.breakin.elasticsearch.document.fieldSpec.job.JobIndexField;
import dev.breakin.elasticsearch.queryBuilder.BoolType;
import dev.breakin.elasticsearch.queryBuilder.queryBuilder.RangeQueryBuilder;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class JobIndexRangeQueryBuilderRegistry {

    private static final Map<JobIndexField, RangeQueryBuilder> builderMap =
            new EnumMap<>(JobIndexField.class);

    static {
        // 숫자/날짜 Range 필드 등록
        register(JobIndexField.MIN_YEARS,        new RangeQueryBuilder(BoolType.FILTER));
        register(JobIndexField.MAX_YEARS,        new RangeQueryBuilder(BoolType.FILTER));
        register(JobIndexField.STARTED_AT,       new RangeQueryBuilder(BoolType.FILTER));
        register(JobIndexField.ENDED_AT,         new RangeQueryBuilder(BoolType.FILTER));
        register(JobIndexField.INTERVIEW_COUNT,  new RangeQueryBuilder(BoolType.FILTER));
        register(JobIndexField.INTERVIEW_DAYS,   new RangeQueryBuilder(BoolType.FILTER));
        register(JobIndexField.CREATED_AT,       new RangeQueryBuilder(BoolType.FILTER));
        register(JobIndexField.UPDATED_AT,       new RangeQueryBuilder(BoolType.FILTER));

        // 인기 지표(nested 하위 필드지만 값 자체는 range 비교)
        register(JobIndexField.POPULARITY_VIEW_COUNT,    new RangeQueryBuilder(BoolType.FILTER));
        register(JobIndexField.POPULARITY_COMMENT_COUNT, new RangeQueryBuilder(BoolType.FILTER));
        register(JobIndexField.POPULARITY_LIKE_COUNT,    new RangeQueryBuilder(BoolType.FILTER));
    }

    private static void register(JobIndexField field, RangeQueryBuilder builder) {
        builderMap.put(field, builder);
    }

    public static Optional<RangeQueryBuilder> getBuilder(JobIndexField field) {
        return Optional.ofNullable(builderMap.get(field));
    }

    public static final Function<JobIndexField, Optional<RangeQueryBuilder>> LOOKUP =
            JobIndexRangeQueryBuilderRegistry::getBuilder;
}