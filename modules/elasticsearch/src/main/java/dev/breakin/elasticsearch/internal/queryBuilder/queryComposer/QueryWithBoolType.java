package dev.breakin.elasticsearch.internal.queryBuilder.queryComposer;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import dev.breakin.elasticsearch.internal.queryBuilder.BoolType;

public record QueryWithBoolType(Query query, BoolType type) {
}
