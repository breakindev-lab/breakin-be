package dev.breakin.jdbc.communitypost.repository;

import dev.breakin.model.common.Popularity;
import dev.breakin.model.communitypost.CommunityPostCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

/**
 * CommunityPost with User DTO
 *
 * LEFT JOIN으로 users 테이블의 nickname을 포함한 CommunityPost 조회 결과 매핑용 DTO
 */
@Getter
@AllArgsConstructor
public class CommunityPostWithUserDto {
    private Long id;
    private Long userId;
    private CommunityPostCategory category;
    private String title;
    private String markdownBody;
    private String company;
    private String location;
    private Long linkedJobId;
    private Boolean isFromJobComment;
    private Long viewCount;
    private Long likeCount;
    private Long dislikeCount;
    private Long commentCount;
    private Boolean isDeleted;
    private Instant createdAt;
    private Instant updatedAt;
    private String nickname;  // From JOIN with users table
}
