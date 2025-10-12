package dev.breakin.elasticsearch.internal.indexer;

import dev.breakin.elasticsearch.document.DocBase;

public interface DocIndexer {
    IndexResponseType indexOne(DocBase doc);
}
