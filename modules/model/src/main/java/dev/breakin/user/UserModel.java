package dev.breakin.user;

import dev.breakin.AuditProps;
import dev.breakin.common.NotificationSettings;
import java.time.Instant;
import java.util.List;

public interface UserModel extends AuditProps {
    Long getUserId();
    String getGoogleId();
    String getEmail();
    String getNickname();
    String getUserRole();
    List<String> getInterestedCompanies();
    List<String> getInterestedLocations();
    NotificationSettings getNotificationSettings();
    Long getPostCount();
    Long getCommentCount();
    Long getLikesReceived();
    Instant getLastLoginAt();
    Boolean getIsActive();
}
