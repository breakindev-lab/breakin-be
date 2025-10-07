package dev.breakin.elasticsearch.queryBuilder.queryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import dev.breakin.elasticsearch.queryBuilder.BoolType;
import dev.breakin.elasticsearch.queryBuilder.FieldQueryBuilder;

public class TermQueryBuilder implements FieldQueryBuilder {

    private final BoolType boolType;

    public TermQueryBuilder(BoolType boolType) {
        this.boolType = boolType;
    }

    @Override
    public Query build(String fieldName, String value) {
        return Query.of(q -> q.term(m -> m.field(fieldName).value(value)));
    }

    @Override
    public BoolType boolType() {
        return boolType;
    }
}