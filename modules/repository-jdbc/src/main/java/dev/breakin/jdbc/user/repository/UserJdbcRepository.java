package dev.breakin.jdbc.user.repository;

import dev.breakin.model.user.User;
import dev.breakin.model.user.UserIdentity;
import dev.breakin.infra.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * User Repository 구현체
 *
 * 헥사고날 아키텍처에서 Adapter 역할을 수행하며,
 * Infrastructure의 UserRepository 인터페이스를
 * Spring Data JDBC를 활용하여 구현합니다.
 */
@Repository
@RequiredArgsConstructor
public class UserJdbcRepository implements UserRepository {

    private final UserEntityRepository entityRepository;

    @Override
    public Optional<User> findById(UserIdentity identity) {
        return entityRepository.findById(identity.getUserId())
                .map(this::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity saved = entityRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public void deleteById(UserIdentity identity) {
        entityRepository.deleteById(identity.getUserId());
    }

    @Override
    public boolean existsById(UserIdentity identity) {
        return entityRepository.existsById(identity.getUserId());
    }

    @Override
    public List<User> findAll() {
        return StreamSupport.stream(entityRepository.findAll().spliterator(), false)
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findByGoogleId(String googleId) {
        return entityRepository.findByGoogleId(googleId)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return entityRepository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        return entityRepository.findByNickname(nickname)
                .map(this::toDomain);
    }

    /**
     * Entity ↔ Domain 변환 메서드
     * Spring Data JDBC가 자동으로 컬렉션과 embedded 객체를 처리
     */
    private User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getGoogleId(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getUserRole(),
                entity.getInterestedCompanies() != null ?
                        entity.getInterestedCompanies().stream()
                                .map(InterestedCompany::getCompanyName)
                                .collect(Collectors.toList()) : List.of(),
                entity.getInterestedLocations() != null ?
                        entity.getInterestedLocations().stream()
                                .map(InterestedLocation::getLocationName)
                                .collect(Collectors.toList()) : List.of(),
                entity.getNotificationSettings(),
                entity.getPostCount(),
                entity.getCommentCount(),
                entity.getLikesReceived(),
                entity.getLastLoginAt(),
                entity.getIsActive(),
                entity.getIsWithdrawn(),
                entity.getWithdrawnAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private UserEntity toEntity(User domain) {
        return new UserEntity(
                domain.getUserId(),
                domain.getGoogleId(),
                domain.getEmail(),
                domain.getNickname(),
                domain.getUserRole(),
                domain.getInterestedCompanies() != null ?
                        domain.getInterestedCompanies().stream()
                                .map(InterestedCompany::new)
                                .collect(Collectors.toSet()) : new HashSet<>(),
                domain.getInterestedLocations() != null ?
                        domain.getInterestedLocations().stream()
                                .map(InterestedLocation::new)
                                .collect(Collectors.toSet()) : new HashSet<>(),
                domain.getNotificationSettings(),
                domain.getPostCount(),
                domain.getCommentCount(),
                domain.getLikesReceived(),
                domain.getLastLoginAt(),
                domain.getIsActive(),
                domain.getIsWithdrawn(),
                domain.getWithdrawnAt(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }
}
