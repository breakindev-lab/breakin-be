package dev.breakin.elasticsearch.internal.queryBuilder.queryBuilder;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import dev.breakin.elasticsearch.internal.queryBuilder.BoolType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dev.breakin.elasticsearch.testutil.ElasticsearchQueryTestHelper.assertOrPrintJson;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * RangeQueryBuilder 테스트
 * <p>
 * Range 쿼리는 범위 검색에 사용 (숫자, 날짜 필드)
 * - min_years, max_years, compensation, started_at, ended_at 등
 */
@DisplayName("RangeQueryBuilder 테스트")
class RangeQueryBuilderTest {

    @Test
    @DisplayName("gte와 lte 모두 있는 경우 range 쿼리 생성")
    void build_withBothGteAndLte_createsRangeQuery() {
        // Given
        RangeQueryBuilder builder = new RangeQueryBuilder(BoolType.FILTER);

        // When
        Query query = builder.build("min_years", "0", "3");

        // Then - 첫 실행 시 비어있음, 출력된 JSON을 복사해서 채우기
        String expectedJson = """
                 {
                  "range" : {
                    "min_years" : {
                      "gte" : "0",
                      "lte" : "3"
                    }
                  }
                }
                """;

        assertOrPrintJson("build_withBothGteAndLte_createsRangeQuery", expectedJson, query);
    }

    @Test
    @DisplayName("gte만 있는 경우 range 쿼리 생성")
    void build_withOnlyGte_createsRangeQueryWithGte() {
        // Given
        RangeQueryBuilder builder = new RangeQueryBuilder(BoolType.FILTER);

        // When
        Query query = builder.build("compensation_min_base_pay", "80000000", null);

        // Then
        String expectedJson = """
                {
                  "range" : {
                    "compensation_min_base_pay" : {
                      "gte" : "80000000"
                    }
                  }
                }
                """;

        assertOrPrintJson("build_withOnlyGte_createsRangeQueryWithGte", expectedJson, query);
    }

    @Test
    @DisplayName("lte만 있는 경우 range 쿼리 생성")
    void build_withOnlyLte_createsRangeQueryWithLte() {
        // Given
        RangeQueryBuilder builder = new RangeQueryBuilder(BoolType.FILTER);

        // When
        Query query = builder.build("max_years", null, "5");

        // Then
        String expectedJson = """
                {
                  "range" : {
                    "max_years" : {
                      "lte" : "5"
                    }
                  }
                }
                """;

        assertOrPrintJson("build_withOnlyLte_createsRangeQueryWithLte", expectedJson, query);
    }

    @Test
    @DisplayName("큰 숫자 값으로 range 쿼리 생성")
    void build_withLargeNumbers_createsRangeQuery() {
        // Given
        RangeQueryBuilder builder = new RangeQueryBuilder(BoolType.FILTER);

        // When
        Query query = builder.build("compensation_max_base_pay", "100000000", "200000000");

        // Then
        String expectedJson = """
                {
                  "range" : {
                    "compensation_max_base_pay" : {
                      "gte" : "100000000",
                      "lte" : "200000000"
                    }
                  }
                }
                """;

        assertOrPrintJson("build_withLargeNumbers_createsRangeQuery", expectedJson, query);
    }

    @Test
    @DisplayName("날짜(timestamp) 값으로 range 쿼리 생성")
    void build_withTimestamp_createsRangeQuery() {
        // Given
        RangeQueryBuilder builder = new RangeQueryBuilder(BoolType.FILTER);

        // When - epochMillis 형식
        Query query = builder.build("started_at", "1696118400000", "1698796800000");

        // Then
        String expectedJson = """
                {
                  "range" : {
                    "started_at" : {
                      "gte" : "1696118400000",
                      "lte" : "1698796800000"
                    }
                  }
                }
                """;

        assertOrPrintJson("build_withTimestamp_createsRangeQuery", expectedJson, query);
    }

    @Test
    @DisplayName("음수 값으로 range 쿼리 생성")
    void build_withNegativeNumbers_createsRangeQuery() {
        // Given
        RangeQueryBuilder builder = new RangeQueryBuilder(BoolType.FILTER);

        // When
        Query query = builder.build("some_field", "-10", "10");

        // Then
        String expectedJson = """
                {
                  "range" : {
                    "some_field" : {
                      "gte" : "-10",
                      "lte" : "10"
                    }
                  }
                }
                """;

        assertOrPrintJson("build_withNegativeNumbers_createsRangeQuery", expectedJson, query);
    }

    @Test
    @DisplayName("FILTER BoolType 반환 확인")
    void boolType_withFilter_returnsFilter() {
        // Given
        RangeQueryBuilder builder = new RangeQueryBuilder(BoolType.FILTER);

        // When & Then
        assertThat(builder.boolType()).isEqualTo(BoolType.FILTER);
    }

    @Test
    @DisplayName("MUST BoolType 반환 확인")
    void boolType_withMust_returnsMust() {
        // Given
        RangeQueryBuilder builder = new RangeQueryBuilder(BoolType.MUST);

        // When & Then
        assertThat(builder.boolType()).isEqualTo(BoolType.MUST);
    }
}
