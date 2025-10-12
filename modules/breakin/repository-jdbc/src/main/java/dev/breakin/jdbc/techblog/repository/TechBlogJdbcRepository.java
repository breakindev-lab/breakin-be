package dev.breakin.jdbc.techblog.repository;

import dev.breakin.model.techblog.TechBlog;
import dev.breakin.model.techblog.TechBlogIdentity;
import dev.breakin.infra.techblog.repository.TechBlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * TechBlog Repository 구현체
 *
 * 헥사고날 아키텍처에서 Adapter 역할을 수행하며,
 * Infrastructure의 TechBlogRepository 인터페이스를
 * Spring Data JDBC를 활용하여 구현합니다.
 */
@Repository
@RequiredArgsConstructor
public class TechBlogJdbcRepository implements TechBlogRepository {

    private final TechBlogEntityRepository entityRepository;

    @Override
    public Optional<TechBlog> findById(TechBlogIdentity identity) {
        return entityRepository.findById(identity.getTechBlogId())
                .map(this::toDomain);
    }

    @Override
    public TechBlog save(TechBlog techBlog) {
        TechBlogEntity entity = toEntity(techBlog);
        TechBlogEntity saved = entityRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(TechBlogIdentity identity) {
        entityRepository.deleteById(identity.getTechBlogId());
    }

    @Override
    public boolean existsById(TechBlogIdentity identity) {
        return entityRepository.existsById(identity.getTechBlogId());
    }

    @Override
    public List<TechBlog> findAll() {
        return StreamSupport.stream(entityRepository.findAll().spliterator(), false)
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TechBlog> findByUrl(String url) {
        return entityRepository.findByUrl(url)
                .map(this::toDomain);
    }

    @Override
    public List<TechBlog> findByCompany(String company) {
        return entityRepository.findByCompany(company).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void increaseViewCount(TechBlogIdentity identity, long increment) {
        entityRepository.increaseViewCount(identity.getTechBlogId(), increment);
    }

    /**
     * Entity ↔ Domain 변환 메서드
     * Spring Data JDBC가 자동으로 컬렉션과 embedded 객체를 처리
     */
    private TechBlog toDomain(TechBlogEntity entity) {
        return new TechBlog(
                entity.getId(),
                entity.getUrl(),
                entity.getCompany(),
                entity.getTitle(),
                entity.getMarkdownBody(),
                entity.getThumbnailUrl(),
                entity.getTags() != null ?
                        entity.getTags().stream()
                                .map(TechBlogTag::getTagName)
                                .collect(Collectors.toList()) : List.of(),
                entity.getTechCategories() != null ?
                        entity.getTechCategories().stream()
                                .map(TechBlogCategory::getCategoryName)
                                .collect(Collectors.toList()) : List.of(),
                entity.getOriginalUrl(),
                entity.getPopularity(),
                entity.getIsDeleted(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private TechBlogEntity toEntity(TechBlog domain) {
        return new TechBlogEntity(
                domain.getTechBlogId(),
                domain.getUrl(),
                domain.getCompany(),
                domain.getTitle(),
                domain.getMarkdownBody(),
                domain.getThumbnailUrl(),
                domain.getTags() != null ?
                        domain.getTags().stream()
                                .map(TechBlogTag::new)
                                .collect(Collectors.toSet()) : new HashSet<>(),
                domain.getTechCategories() != null ?
                        domain.getTechCategories().stream()
                                .map(TechBlogCategory::new)
                                .collect(Collectors.toSet()) : new HashSet<>(),
                domain.getOriginalUrl(),
                domain.getPopularity(),
                domain.getIsDeleted(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}
