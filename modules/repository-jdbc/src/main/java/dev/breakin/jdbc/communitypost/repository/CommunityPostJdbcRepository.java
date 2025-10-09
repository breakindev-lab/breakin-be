package dev.breakin.jdbc.communitypost.repository;

import dev.breakin.model.communitypost.CommunityPost;
import dev.breakin.model.communitypost.CommunityPostIdentity;
import dev.breakin.model.communitypost.CommunityPostRead;
import dev.breakin.model.common.Popularity;
import dev.breakin.infra.communitypost.repository.CommunityPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * CommunityPost Repository 구현체
 *
 * 헥사고날 아키텍처에서 Adapter 역할을 수행하며,
 * Infrastructure의 CommunityPostRepository 인터페이스를
 * Spring Data JDBC를 활용하여 구현합니다.
 */
@Repository
@RequiredArgsConstructor
public class CommunityPostJdbcRepository implements CommunityPostRepository {

    private final CommunityPostEntityRepository entityRepository;

    @Override
    public Optional<CommunityPostRead> findById(CommunityPostIdentity identity) {
        CommunityPostWithUserDto dto = entityRepository.findByIdWithUser(identity.getCommunityPostId());
        return Optional.ofNullable(dto).map(this::toCommunityPostRead);
    }

    @Override
    public CommunityPost save(CommunityPost communityPost) {
        CommunityPostEntity entity = toEntity(communityPost);
        CommunityPostEntity saved = entityRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(CommunityPostIdentity identity) {
        entityRepository.deleteById(identity.getCommunityPostId());
    }

    @Override
    public boolean existsById(CommunityPostIdentity identity) {
        return entityRepository.existsById(identity.getCommunityPostId());
    }

    @Override
    public List<CommunityPostRead> findAll() {
        return entityRepository.findAllWithUser().stream()
                .map(this::toCommunityPostRead)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommunityPostRead> findByUserId(Long userId) {
        return entityRepository.findByUserIdWithUser(userId).stream()
                .map(this::toCommunityPostRead)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommunityPostRead> findByCompany(String company) {
        return entityRepository.findByCompanyWithUser(company).stream()
                .map(this::toCommunityPostRead)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommunityPostRead> findByLocation(String location) {
        return entityRepository.findByLocationWithUser(location).stream()
                .map(this::toCommunityPostRead)
                .collect(Collectors.toList());
    }

    @Override
    public void increaseViewCount(CommunityPostIdentity identity, long increment) {
        entityRepository.increaseViewCount(identity.getCommunityPostId(), increment);
    }
    /**
     * DTO → CommunityPostRead 변환 (LEFT JOIN 결과 with nickname)
     */
    private CommunityPostRead toCommunityPostRead(CommunityPostWithUserDto dto) {
        Popularity popularity = new Popularity(
                dto.getViewCount(),
                dto.getCommentCount(),
                dto.getLikeCount(),
                dto.getDislikeCount()
        );

        return new CommunityPostRead(
                dto.getId(),
                dto.getUserId(),
                dto.getNickname(),
                dto.getCategory(),
                dto.getTitle(),
                dto.getMarkdownBody(),
                dto.getCompany(),
                dto.getLocation(),
                dto.getLinkedJobId(),
                dto.getIsFromJobComment(),
                popularity,
                dto.getIsDeleted(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }

    /**
     * Entity ↔ Domain 변환 메서드
     * Spring Data JDBC가 자동으로 embedded 객체를 처리
     */
    private CommunityPost toDomain(CommunityPostEntity entity) {
        return new CommunityPost(
                entity.getId(),
                entity.getUserId(),
                entity.getCategory(),
                entity.getTitle(),
                entity.getMarkdownBody(),
                entity.getCompany(),
                entity.getLocation(),
                entity.getLinkedJobId(),
                entity.getIsFromJobComment(),
                entity.getPopularity(),
                entity.getIsDeleted(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private CommunityPostEntity toEntity(CommunityPost domain) {
        return new CommunityPostEntity(
                domain.getCommunityPostId(),
                domain.getUserId(),
                domain.getCategory(),
                domain.getTitle(),
                domain.getMarkdownBody(),
                domain.getCompany(),
                domain.getLocation(),
                domain.getLinkedJobId(),
                domain.getIsFromJobComment(),
                domain.getPopularity(),
                domain.getIsDeleted(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}
