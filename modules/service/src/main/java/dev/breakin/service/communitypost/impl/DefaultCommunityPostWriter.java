package dev.breakin.service.communitypost.impl;

import dev.breakin.exception.communitypost.CommunityPostNotFoundException;
import dev.breakin.model.communitypost.CommunityPost;
import dev.breakin.model.communitypost.CommunityPostIdentity;
import dev.breakin.model.communitypost.CommunityPostRead;
import dev.breakin.service.communitypost.CommunityPostWriter;
import dev.breakin.infra.communitypost.repository.CommunityPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * CommunityPost 도메인 변경 서비스 구현체
 *
 * CQRS 패턴의 Command 책임을 구현하며,
 * Infrastructure Repository를 활용한 변경 로직을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultCommunityPostWriter implements CommunityPostWriter {

    private final CommunityPostRepository communityPostRepository;

    @Override
    public CommunityPost upsert(CommunityPost communityPost) {
        log.info("Upserting CommunityPost: {}", communityPost.getCommunityPostId());
        CommunityPost saved = communityPostRepository.save(communityPost);
        log.info("CommunityPost upserted successfully: {}", saved.getCommunityPostId());
        return saved;
    }

    @Override
    public void delete(CommunityPostIdentity identity) {
        log.info("Deleting CommunityPost by id: {}", identity.getCommunityPostId());
        communityPostRepository.deleteById(identity);
        log.info("CommunityPost deleted successfully: {}", identity.getCommunityPostId());
    }

    /**
     * CommunityPostRead → CommunityPost 변환 (nickname 제외)
     */
    private CommunityPost toCommunityPost(CommunityPostRead communityPostRead) {
        return new CommunityPost(
                communityPostRead.getCommunityPostId(),
                communityPostRead.getUserId(),
                communityPostRead.getCategory(),
                communityPostRead.getTitle(),
                communityPostRead.getMarkdownBody(),
                communityPostRead.getCompany(),
                communityPostRead.getLocation(),
                communityPostRead.getLinkedJobId(),
                communityPostRead.getIsFromJobComment(),
                communityPostRead.getPopularity(),
                communityPostRead.getIsDeleted(),
                communityPostRead.getCreatedAt(),
                communityPostRead.getUpdatedAt()
        );
    }
}
