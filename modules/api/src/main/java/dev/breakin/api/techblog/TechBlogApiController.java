package dev.breakin.api.techblog;

import dev.breakin.api.techblog.dto.TechBlogRequest;
import dev.breakin.api.techblog.dto.TechBlogResponse;
import dev.breakin.model.techblog.TechBlog;
import dev.breakin.model.techblog.TechBlogIdentity;
import dev.breakin.service.techblog.TechBlogReader;
import dev.breakin.service.techblog.TechBlogWriter;
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
 * TechBlog REST API 컨트롤러
 *
 * Reader/Writer 패턴을 활용하여 CQRS 기반 API를 제공합니다.
 */
@RestController
@RequestMapping("/api/tech-blogs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tech Blog", description = "Tech blog API")
public class TechBlogApiController {

    private final TechBlogReader techBlogReader;
    private final TechBlogWriter techBlogWriter;

    /**
     * 기술 블로그 조회
     * GET /api/tech-blogs/{techBlogId}
     */
    @Operation(summary = "Get tech blog", description = "Retrieve a tech blog by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "404", description = "Tech blog not found")
    })
    @GetMapping("/{techBlogId}")
    public ResponseEntity<TechBlogResponse> getTechBlog(
            @Parameter(description = "Tech blog ID", example = "1") @PathVariable Long techBlogId) {
        log.info("GET /api/tech-blogs/{}", techBlogId);

        TechBlog techBlog = techBlogReader.read(new TechBlogIdentity(techBlogId));
        TechBlogResponse response = TechBlogResponse.from(techBlog);

        log.info("Retrieved tech blog - techBlogId: {}", response.getTechBlogId());
        return ResponseEntity.ok(response);
    }

    /**
     * 기술 블로그 생성/수정
     * POST /api/tech-blogs
     */
    @Operation(summary = "Create or update tech blog", description = "Create a new tech blog or update existing one")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Successfully created or updated"),
        @ApiResponse(responseCode = "400", description = "Validation failed")
    })
    @PostMapping
    public ResponseEntity<TechBlogResponse> upsertTechBlog(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Tech blog request", required = true)
            @Valid @RequestBody TechBlogRequest request) {
        log.info("POST /api/tech-blogs - title: {}", request.getTitle());

        TechBlog techBlog = request.toTechBlog();
        TechBlog saved = techBlogWriter.upsert(techBlog);
        TechBlogResponse response = TechBlogResponse.from(saved);

        log.info("Tech blog upserted - techBlogId: {}", response.getTechBlogId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 기술 블로그 삭제
     * DELETE /api/tech-blogs/{techBlogId}
     */
    @Operation(summary = "Delete tech blog", description = "Delete a tech blog by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Tech blog not found")
    })
    @DeleteMapping("/{techBlogId}")
    public ResponseEntity<Void> deleteTechBlog(
            @Parameter(description = "Tech blog ID", example = "1") @PathVariable Long techBlogId) {
        log.info("DELETE /api/tech-blogs/{}", techBlogId);

        techBlogWriter.delete(new TechBlogIdentity(techBlogId));

        log.info("Tech blog deleted - techBlogId: {}", techBlogId);
        return ResponseEntity.noContent().build();
    }
}
