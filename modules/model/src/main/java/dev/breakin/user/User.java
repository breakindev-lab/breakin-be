package dev.breakin.user;

import dev.breakin.common.NotificationSettings;
import lombok.Value;
import java.time.Instant;
import java.util.List;

@Value
public class User implements UserModel {
    Long userId;
    String googleId;
    String email;
    String nickname;
    String userRole;
    List<String> interestedCompanies;
    List<String> interestedLocations;
    NotificationSettings notificationSettings;
    Long postCount;
    Long commentCount;
    Long likesReceived;
    Instant lastLoginAt;
    Boolean isActive;
    Instant createdAt;
    Instant updatedAt;

    public static User newUser(String googleId, String email, String nickname) {
        Instant now = Instant.now();
        return new User(
            null,
            googleId,
            email,
            nickname,
            "USER",
            List.of(),
            List.of(),
            NotificationSettings.defaultSettings(),
            0L,
            0L,
            0L,
            now,
            true,
            now,
            now
        );
    }

    public User updateLastLogin() {
        return new User(
            userId, googleId, email, nickname, userRole,
            interestedCompanies, interestedLocations, notificationSettings,
            postCount, commentCount, likesReceived,
            Instant.now(), isActive, createdAt, Instant.now()
        );
    }

    public User incrementPostCount() {
        return new User(
            userId, googleId, email, nickname, userRole,
            interestedCompanies, interestedLocations, notificationSettings,
            postCount + 1, commentCount, likesReceived,
            lastLoginAt, isActive, createdAt, Instant.now()
        );
    }

    public User incrementCommentCount() {
        return new User(
            userId, googleId, email, nickname, userRole,
            interestedCompanies, interestedLocations, notificationSettings,
            postCount, commentCount + 1, likesReceived,
            lastLoginAt, isActive, createdAt, Instant.now()
        );
    }
}
