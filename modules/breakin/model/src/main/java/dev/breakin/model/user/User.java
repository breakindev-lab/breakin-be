package dev.breakin.model.user;

import dev.breakin.model.AuditProps;
import dev.breakin.model.common.NotificationSettings;
import lombok.Value;
import java.time.Instant;
import java.util.List;

@Value
public class User implements AuditProps {
    Long userId;
    String googleId;
    String email;
    String nickname;
    UserRole userRole;
    List<String> interestedCompanies;
    List<String> interestedLocations;
    NotificationSettings notificationSettings;

    Long postCount;
    Long commentCount;
    Long likesReceived;

    Instant lastLoginAt;
    Boolean isActive;
    Boolean isWithdrawn;
    Instant withdrawnAt;

    Instant createdAt;
    Instant updatedAt;

    public static User newUser(String googleId, String email, String nickname) {
        Instant now = Instant.now();
        return new User(
            null,
            googleId,
            email,
            nickname,
            UserRole.USER,
            List.of(),
            List.of(),
            NotificationSettings.defaultSettings(),
            0L,
            0L,
            0L,
            now,
            true,
            false,
            null,
            now,
            now
        );
    }

    public User updateLastLogin() {
        return new User(
            userId, googleId, email, nickname, userRole,
            interestedCompanies, interestedLocations, notificationSettings,
            postCount, commentCount, likesReceived,
            Instant.now(), isActive, isWithdrawn, withdrawnAt, createdAt, Instant.now()
        );
    }

    public User incrementPostCount() {
        return new User(
            userId, googleId, email, nickname, userRole,
            interestedCompanies, interestedLocations, notificationSettings,
            postCount + 1, commentCount, likesReceived,
            lastLoginAt, isActive, isWithdrawn, withdrawnAt, createdAt, Instant.now()
        );
    }

    public User incrementCommentCount() {
        return new User(
            userId, googleId, email, nickname, userRole,
            interestedCompanies, interestedLocations, notificationSettings,
            postCount, commentCount + 1, likesReceived,
            lastLoginAt, isActive, isWithdrawn, withdrawnAt, createdAt, Instant.now()
        );
    }

    public User withdraw() {
        return new User(
            userId, googleId, email, nickname, userRole,
            interestedCompanies, interestedLocations, notificationSettings,
            postCount, commentCount, likesReceived,
            lastLoginAt, isActive, true, Instant.now(), createdAt, Instant.now()
        );
    }
}
