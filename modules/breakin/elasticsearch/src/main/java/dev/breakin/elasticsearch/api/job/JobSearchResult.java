package dev.breakin.elasticsearch.api.job;


import dev.breakin.elasticsearch.document.JobDoc;

import java.util.List;

public record JobSearchResult(
        List<JobDoc> docs,
        boolean hasNext
) {
}

