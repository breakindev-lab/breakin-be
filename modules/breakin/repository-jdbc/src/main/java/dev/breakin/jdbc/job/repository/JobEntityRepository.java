package dev.breakin.jdbc.job.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Job Entity CRUD API 인터페이스
 *
 * Spring Data JDBC를 활용한 JobEntity 데이터 접근 계층
 * Infrastructure Repository 인터페이스 기반으로 필요한 메서드만 생성
 */
@Repository
public interface JobEntityRepository extends CrudRepository<JobEntity, Long> {
    Optional<JobEntity> findByUrl(String url);
    List<JobEntity> findByCompany(String company);

    @Modifying
    @Query("UPDATE jobs SET view_count = view_count + :increment WHERE id = :jobId")
    void increaseViewCount(@Param("jobId") Long jobId, @Param("increment") long increment);
}
