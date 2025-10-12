package dev.breakin.elasticsearch.query.job;

import dev.breakin.elasticsearch.document.JobDoc;
import dev.breakin.elasticsearch.queryBuilder.GenericSearchQueryBuilder;
import dev.breakin.elasticsearch.queryBuilder.SearchCommand;
import dev.breakin.elasticsearch.queryBuilder.queryExecutor.SearchQueryExecutor;
import dev.breakin.elasticsearch.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JobSearch {

    @Value("${es.index.hiring}")
    private String JOB_INDEX;

    private final SearchQueryExecutor executor;
    private static final Integer DEFAULT_PAGE_SIZE = 30;

    private JobSearchResult search(SearchCommand command) {
        var q = GenericSearchQueryBuilder.build(command,
                JobIndexQueryBuilderRegistry.LOOKUP,
                JobIndexRangeQueryBuilderRegistry.LOOKUP);

        var pagination = PaginationUtils.calculatePaginationInfo(command.from(), command.to(), DEFAULT_PAGE_SIZE);

        var docs = executor.search(JOB_INDEX, q, pagination.from(), pagination.searchSize(), JobDoc.class);
        var result = PaginationUtils.paginate(docs, pagination.requestedSize());

        return new JobSearchResult(result.data(), result.hasNext());
    }
}

