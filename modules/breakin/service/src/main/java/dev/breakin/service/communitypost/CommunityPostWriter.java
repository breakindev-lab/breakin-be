package dev.breakin.service.communitypost;

import dev.breakin.model.communitypost.CommunityPost;
import dev.breakin.model.communitypost.CommunityPostIdentity;

/**
 * CommunityPost 도메인 변경 서비스 인터페이스
 *
 * CQRS 패턴의 Command 책임을 담당하며,
 * Infrastructure Repository 기반으로 변경 로직을 제공합니다.
 */
public interface CommunityPostWriter {

    /**
     * CommunityPost 저장 (생성/수정)
     *
     * @param communityPost 저장할 CommunityPost
     * @return 저장된 CommunityPost
     */
    CommunityPost upsert(CommunityPost communityPost);

    /**
     * ID로 CommunityPost 삭제
     *
     * @param identity CommunityPost 식별자
     */
    void delete(CommunityPostIdentity identity);
}
