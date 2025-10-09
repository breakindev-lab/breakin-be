package dev.breakin.service.user.impl;

import dev.breakin.exception.user.UserNotFoundException;
import dev.breakin.model.user.User;
import dev.breakin.model.user.UserIdentity;
import dev.breakin.service.user.UserReader;
import dev.breakin.infra.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User 도메인 조회 서비스 구현체
 * <p>
 * CQRS 패턴의 Query 책임을 구현하며,
 * Infrastructure Repository를 활용한 조회 로직을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultUserReader implements UserReader {

    private final UserRepository userRepository;

    @Override
    public User getById(UserIdentity identity) {
        log.debug("Fetching User by id: {}", identity.getUserId());
        return userRepository.findById(identity)
                .orElseThrow(() -> new UserNotFoundException("identity : " + identity.getUserId()));
    }

    @Override
    public List<User> getAll() {
        log.debug("Fetching all Users");
        return userRepository.findAll();
    }

    @Override
    public User getByGoogleId(String googleId) {
        log.debug("Fetching User by googleId: {}", googleId);
        return userRepository.findByGoogleId(googleId)
                .orElseThrow(() -> new UserNotFoundException("Google ID: " + googleId));
    }

    @Override
    public User getByEmail(String email) {
        log.debug("Fetching User by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Email: " + email));
    }

    @Override
    public User getByNickname(String nickname) {
        log.debug("Fetching User by nickname: {}", nickname);
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new UserNotFoundException("Nickname: " + nickname));
    }
}
