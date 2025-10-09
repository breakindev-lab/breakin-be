package dev.breakin.jdbc.user.repository;

import dev.breakin.model.common.NotificationSettings;
import dev.breakin.model.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Set;

/**
 * User Spring Data JDBC Entity
 *
 * Model 클래스 스펙을 기반으로 생성된 데이터베이스 매핑용 엔티티
 * - NotificationSettings: @Embedded로 users 테이블에 컬럼으로 저장
 * - interestedCompanies/Locations: 별도 테이블로 정규화
 */
@Table("users")
@Getter
@AllArgsConstructor
public class UserEntity {
    @Id
    private Long id;
    private String googleId;
    private String email;
    private String nickname;
    private UserRole userRole;

    // 1:N 관계 - user_interested_companies 테이블로 분리
    @MappedCollection(idColumn = "user_id", keyColumn = "user_id")
    private Set<InterestedCompany> interestedCompanies;

    // 1:N 관계 - user_interested_locations 테이블로 분리
    @MappedCollection(idColumn = "user_id", keyColumn = "user_id")
    private Set<InterestedLocation> interestedLocations;

    // Embedded - users 테이블에 컬럼으로 flatten
    @Embedded.Nullable
    private NotificationSettings notificationSettings;

    private Long postCount;
    private Long commentCount;
    private Long likesReceived;
    private Instant lastLoginAt;
    private Boolean isActive;
    private Boolean isWithdrawn;
    private Instant withdrawnAt;
    private Instant createdAt;
    private Instant updatedAt;
}
