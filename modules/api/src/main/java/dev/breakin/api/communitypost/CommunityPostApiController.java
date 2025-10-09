package dev.breakin.api.communitypost;

import dev.breakin.api.communitypost.dto.CommunityPostRequest;
import dev.breakin.api.communitypost.dto.CommunityPostResponse;
import dev.breakin.model.communitypost.CommunityPost;
import dev.breakin.model.communitypost.CommunityPostIdentity;
import dev.breakin.model.communitypost.CommunityPostRead;
import dev.breakin.service.communitypost.CommunityPostReader;
import dev.breakin.service.communitypost.CommunityPostWriter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * CommunityPost REST API 컨트롤러
 *
 * Reader/Writer 패턴을 활용하여 CQRS 기반 API를 제공합니다.
 */
@RestController
@RequestMapping("/api/community-posts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Community Post", description = "Community post API")
public class CommunityPostApiController {

    private final CommunityPostReader communityPostReader;
    private final CommunityPostWriter communityPostWriter;

    /**
     * 커뮤니티 게시글 조회
     * GET /api/community-posts/{communityPostId}
     */
    @Operation(summary = "Get community post", description = "Retrieve a community post by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @GetMapping("/{communityPostId}")
    public ResponseEntity<CommunityPostResponse> getCommunityPost(
            @Parameter(description = "Community post ID", example = "1") @PathVariable Long communityPostId) {
        log.info("GET /api/community-posts/{}", communityPostId);

        CommunityPostRead communityPostRead = communityPostReader.read(new CommunityPostIdentity(communityPostId));
        CommunityPostResponse response = CommunityPostResponse.from(communityPostRead);

        log.info("Retrieved community post - communityPostId: {}", response.getCommunityPostId());
        return ResponseEntity.ok(response);
    }

    /**
     * 커뮤니티 게시글 생성/수정
     * POST /api/community-posts
     */
    @Operation(summary = "Create or update community post", description = "Create a new community post or update existing one")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Successfully created or updated"),
        @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PostMapping
    public ResponseEntity<CommunityPostResponse> upsertCommunityPost(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Community post request", required = true)
            @Valid @RequestBody CommunityPostRequest request) {
        log.info("POST /api/community-posts - userId: {}, category: {}",
                request.getUserId(), request.getCategory());

        CommunityPost communityPost = request.toCommunityPost();
        CommunityPost saved = communityPostWriter.upsert(communityPost);

        // Re-fetch with user information for response
        CommunityPostRead communityPostRead = communityPostReader.getById(new CommunityPostIdentity(saved.getCommunityPostId()));
        CommunityPostResponse response = CommunityPostResponse.from(communityPostRead);

        log.info("Community post upserted - communityPostId: {}", response.getCommunityPostId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 커뮤니티 게시글 삭제
     * DELETE /api/community-posts/{communityPostId}
     */
    @Operation(summary = "Delete community post", description = "Delete a community post by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Post not found")
    })
    @DeleteMapping("/{communityPostId}")
    public ResponseEntity<Void> deleteCommunityPost(
            @Parameter(description = "Community post ID", example = "1") @PathVariable Long communityPostId) {
        log.info("DELETE /api/community-posts/{}", communityPostId);

        communityPostWriter.delete(new CommunityPostIdentity(communityPostId));

        log.info("Community post deleted - communityPostId: {}", communityPostId);
        return ResponseEntity.noContent().build();
    }
}
