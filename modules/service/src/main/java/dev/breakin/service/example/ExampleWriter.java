package dev.breakin.service.example;

import dev.breakin.example.Example;
import dev.breakin.example.ExampleIdentity;

/**
 * Example 도메인 변경 서비스 인터페이스
 *
 * CQRS 패턴의 Command 책임을 담당합니다.
 */
public interface ExampleWriter {

    /**
     * Example 생성 또는 수정 (upsert)
     *
     * @param example 저장할 Example
     * @return 저장된 Example
     */
    Example upsert(Example example);

    /**
     * ID로 Example 삭제
     *
     * @param identity Example 식별자
     */
    void delete(ExampleIdentity identity);
}
