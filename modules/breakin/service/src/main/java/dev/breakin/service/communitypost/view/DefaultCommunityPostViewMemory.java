package dev.breakin.service.communitypost.view;

import dev.breakin.infra.communitypost.repository.CommunityPostRepository;
import dev.breakin.model.communitypost.CommunityPost;
import dev.breakin.model.communitypost.CommunityPostIdentity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * CommunityPost 조회수 메모리 관리 구현체
 *
 * ConcurrentHashMap과 AtomicLong을 활용하여 동시성을 보장하며,
 * 메모리에 조회수를 누적한 후 주기적으로 DB에 일괄 반영합니다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultCommunityPostViewMemory implements CommunityPostViewMemory {

    private final CommunityPostRepository communityPostRepository;

    /**
     * 메모리에 누적된 조회수 저장소
     * Key: CommunityPost ID, Value: 조회수 증가량
     */
    private final Map<Long, AtomicLong> viewCounts = new ConcurrentHashMap<>();

    @Override
    public void countUp(Long communityPostId) {
        if (communityPostId == null) {
            log.warn("CommunityPostId is null, skipping countUp");
            return;
        }

        viewCounts.computeIfAbsent(communityPostId, k -> new AtomicLong(0))
                .incrementAndGet();

        log.debug("View count incremented for CommunityPost: {}", communityPostId);
    }

    @Override
    @Scheduled(fixedDelay = 10000) // 10초마다 실행
    public void flush() {
        if (viewCounts.isEmpty()) {
            log.debug("No view counts to flush");
            return;
        }

        log.info("Starting to flush {} CommunityPost view counts", viewCounts.size());
        int successCount = 0;
        int failCount = 0;

        // 현재 메모리의 스냅샷을 추출하고 초기화
        Map<Long, AtomicLong> snapshot = new ConcurrentHashMap<>(viewCounts);
        viewCounts.clear();

        for (Map.Entry<Long, AtomicLong> entry : snapshot.entrySet()) {
            Long communityPostId = entry.getKey();
            long incrementCount = entry.getValue().get();

            if (incrementCount <= 0) {
                continue;
            }

            try {
                CommunityPostIdentity identity = new CommunityPostIdentity(communityPostId);

                // DB 레벨에서 원자적으로 조회수 증가
                communityPostRepository.increaseViewCount(identity, incrementCount);
                successCount++;
                log.debug("Flushed {} view counts for CommunityPost: {}", incrementCount, communityPostId);

            } catch (Exception e) {
                log.error("Failed to flush view count for CommunityPost: {}", communityPostId, e);
                failCount++;
            }
        }

        log.info("Flush completed - Success: {}, Failed: {}", successCount, failCount);
    }
}
