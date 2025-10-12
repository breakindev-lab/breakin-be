package dev.breakin.elasticsearch.indexer;

import dev.breakin.elasticsearch.document.DocBase;

public interface DocIndexer {
    IndexResponseType indexOne(DocBase doc);
}
