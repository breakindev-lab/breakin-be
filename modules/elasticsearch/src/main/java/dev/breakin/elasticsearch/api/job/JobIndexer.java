package dev.breakin.elasticsearch.api.job;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import dev.breakin.elasticsearch.document.DocBase;
import dev.breakin.elasticsearch.internal.indexer.AbstractDocIndexer;
import org.springframework.stereotype.Component;

@Component
public class JobIndexer extends AbstractDocIndexer {
    public JobIndexer(ElasticsearchClient esClient) {
        super(esClient);
    }

    @Override
    protected String getIndex() {
        return "breakin-dev-job";
    }

    @Override
    protected String getDocId(DocBase doc) {
        return doc.getDocId();
    }

    @Override
    protected boolean validateDoc(DocBase doc) {
        return doc.getDocId() != null;
    }
}
