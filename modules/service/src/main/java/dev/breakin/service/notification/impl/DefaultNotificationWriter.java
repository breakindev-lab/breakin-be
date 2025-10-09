package dev.breakin.service.notification.impl;

import dev.breakin.model.notification.Notification;
import dev.breakin.model.notification.NotificationIdentity;
import dev.breakin.service.notification.NotificationWriter;
import dev.breakin.infra.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Notification 도메인 변경 서비스 구현체
 *
 * CQRS 패턴의 Command 책임을 구현하며,
 * Infrastructure Repository를 활용한 변경 로직을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultNotificationWriter implements NotificationWriter {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification upsert(Notification notification) {
        log.info("Upserting Notification: {}", notification.getNotificationId());
        Notification saved = notificationRepository.save(notification);
        log.info("Notification upserted successfully: {}", saved.getNotificationId());
        return saved;
    }

    @Override
    public void delete(NotificationIdentity identity) {
        log.info("Deleting Notification by id: {}", identity.getNotificationId());
        notificationRepository.deleteById(identity);
        log.info("Notification deleted successfully: {}", identity.getNotificationId());
    }
}
