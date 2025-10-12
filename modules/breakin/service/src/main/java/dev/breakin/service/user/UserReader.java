package dev.breakin.service.user;

import dev.breakin.model.user.User;
import dev.breakin.model.user.UserIdentity;

import java.util.List;

/**
 * User 도메인 조회 서비스 인터페이스
 *
 * CQRS 패턴의 Query 책임을 담당하며,
 * Infrastructure Repository 기반으로 조회 로직을 제공합니다.
 */
public interface UserReader {

    /**
     * ID로 User 조회
     *
     * @param identity User 식별자
     * @return User 엔티티
     */
    User getById(UserIdentity identity);

    /**
     * 모든 User 조회
     *
     * @return User 목록
     */
    List<User> getAll();

    /**
     * Google ID로 User 조회
     *
     * @param googleId Google 식별자
     * @return User 엔티티
     */
    User getByGoogleId(String googleId);

    /**
     * 이메일로 User 조회
     *
     * @param email 이메일 주소
     * @return User 엔티티
     */
    User getByEmail(String email);

    /**
     * 닉네임으로 User 조회
     *
     * @param nickname 사용자 닉네임
     * @return User 엔티티
     */
    User getByNickname(String nickname);
}
