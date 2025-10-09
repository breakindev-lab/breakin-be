package dev.breakin.service.user.impl;

import dev.breakin.model.user.User;
import dev.breakin.model.user.UserIdentity;
import dev.breakin.service.user.UserWriter;
import dev.breakin.infra.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * User 도메인 변경 서비스 구현체
 *
 * CQRS 패턴의 Command 책임을 구현하며,
 * Infrastructure Repository를 활용한 변경 로직을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultUserWriter implements UserWriter {

    private final UserRepository userRepository;

    @Override
    public User upsert(User user) {
        log.info("Upserting User: {}", user.getUserId());
        User saved = userRepository.save(user);
        log.info("User upserted successfully: {}", saved.getUserId());
        return saved;
    }

    @Override
    public void delete(UserIdentity identity) {
        log.info("Deleting User by id: {}", identity.getUserId());
        userRepository.deleteById(identity);
        log.info("User deleted successfully: {}", identity.getUserId());
    }

    @Override
    public User withdraw(UserIdentity identity) {
        return null;
    }
}
