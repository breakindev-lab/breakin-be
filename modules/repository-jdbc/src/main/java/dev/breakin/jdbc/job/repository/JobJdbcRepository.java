package dev.breakin.jdbc.job.repository;

import dev.breakin.model.job.Job;
import dev.breakin.model.job.JobIdentity;
import dev.breakin.infra.job.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Job Repository 구현체
 *
 * 헥사고날 아키텍처에서 Adapter 역할을 수행하며,
 * Infrastructure의 JobRepository 인터페이스를
 * Spring Data JDBC를 활용하여 구현합니다.
 */
@Repository
@RequiredArgsConstructor
public class JobJdbcRepository implements JobRepository {

    private final JobEntityRepository entityRepository;

    @Override
    public Optional<Job> findById(JobIdentity identity) {
        return entityRepository.findById(identity.getJobId())
                .map(this::toDomain);
    }

    @Override
    public Job save(Job job) {
        JobEntity entity = toEntity(job);
        JobEntity saved = entityRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(JobIdentity identity) {
        entityRepository.deleteById(identity.getJobId());
    }

    @Override
    public boolean existsById(JobIdentity identity) {
        return entityRepository.existsById(identity.getJobId());
    }

    @Override
    public List<Job> findAll() {
        return StreamSupport.stream(entityRepository.findAll().spliterator(), false)
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Job> findByUrl(String url) {
        return entityRepository.findByUrl(url)
                .map(this::toDomain);
    }

    @Override
    public List<Job> findByCompany(String company) {
        return entityRepository.findByCompany(company).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void increaseViewCount(JobIdentity identity, long increment) {
        entityRepository.increaseViewCount(identity.getJobId(), increment);
    }

    /**
     * Entity ↔ Domain 변환 메서드
     * Spring Data JDBC가 자동으로 컬렉션과 embedded 객체를 처리
     */
    private Job toDomain(JobEntity entity) {
        return new Job(
                entity.getId(),
                entity.getUrl(),
                entity.getCompany(),
                entity.getTitle(),
                entity.getOrganization(),
                entity.getMarkdownBody(),
                entity.getOneLineSummary(),
                entity.getMinYears(),
                entity.getMaxYears(),
                entity.getExperienceRequired(),
                entity.getCareerLevel(),
                entity.getEmploymentType(),
                entity.getPositionCategory(),
                entity.getRemotePolicy(),
                entity.getTechCategories() != null ?
                        entity.getTechCategories().stream()
                                .map(JobTechCategory::getCategoryName)
                                .collect(Collectors.toList()) : List.of(),
                entity.getStartedAt(),
                entity.getEndedAt(),
                entity.getIsOpenEnded(),
                entity.getIsClosed(),
                entity.getLocation(),
                entity.getHasAssignment(),
                entity.getHasCodingTest(),
                entity.getHasLiveCoding(),
                entity.getInterviewCount(),
                entity.getInterviewDays(),
                entity.getPopularity(),
                entity.getIsDeleted(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private JobEntity toEntity(Job domain) {
        return new JobEntity(
                domain.getJobId(),
                domain.getUrl(),
                domain.getCompany(),
                domain.getTitle(),
                domain.getOrganization(),
                domain.getMarkdownBody(),
                domain.getOneLineSummary(),
                domain.getMinYears(),
                domain.getMaxYears(),
                domain.getExperienceRequired(),
                domain.getCareerLevel(),
                domain.getEmploymentType(),
                domain.getPositionCategory(),
                domain.getRemotePolicy(),
                domain.getTechCategories() != null ?
                        domain.getTechCategories().stream()
                                .map(JobTechCategory::new)
                                .collect(Collectors.toSet()) : new HashSet<>(),
                domain.getStartedAt(),
                domain.getEndedAt(),
                domain.getIsOpenEnded(),
                domain.getIsClosed(),
                domain.getLocation(),
                domain.getHasAssignment(),
                domain.getHasCodingTest(),
                domain.getHasLiveCoding(),
                domain.getInterviewCount(),
                domain.getInterviewDays(),
                domain.getPopularity(),
                domain.getIsDeleted(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}
