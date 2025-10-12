package dev.breakin.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

/**
 * 세션에 저장되는 간이 유저 정보
 *
 * SecurityContext에 저장되어 인증된 사용자 정보를 표현합니다.
 */
@Getter
@AllArgsConstructor
public class SessionUser {
    private final Long userId;
    private final String email;
    private final Instant loginAt;

    /**
     * 세션 유저 생성
     */
    public static SessionUser of(Long userId, String email) {
        return new SessionUser(userId, email, Instant.now());
    }
}
