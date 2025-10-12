package dev.breakin.elasticsearch.internal.query.job;


import dev.breakin.elasticsearch.document.JobDoc;

import java.util.List;

public record JobSearchResult(
        List<JobDoc> docs,
        boolean hasNext
) {
}

