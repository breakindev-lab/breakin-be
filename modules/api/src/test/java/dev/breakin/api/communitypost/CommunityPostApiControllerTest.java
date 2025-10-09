package dev.breakin.api.communitypost;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.breakin.api.communitypost.dto.CommunityPostResponse;
import dev.breakin.model.common.Popularity;
import dev.breakin.model.communitypost.CommunityPost;
import dev.breakin.model.communitypost.CommunityPostCategory;
import dev.breakin.model.communitypost.CommunityPostIdentity;
import dev.breakin.model.communitypost.CommunityPostRead;
import dev.breakin.service.communitypost.CommunityPostReader;
import dev.breakin.service.communitypost.CommunityPostWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CommunityPostApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommunityPostReader communityPostReader;

    @Mock
    private CommunityPostWriter communityPostWriter;

    @InjectMocks
    private CommunityPostApiController communityPostApiController;

    private ObjectMapper objectMapper;

    // CommunityPost for write operations
    private static final CommunityPost sampleCommunityPost = new CommunityPost(
            1L,                                  // communityPostId
            1L,                                  // userId
            CommunityPostCategory.INTERVIEW_SHARE, // category
            "Test Community Post Title",         // title
            "Test markdown body content",        // markdownBody
            "Test Company",                      // company
            "Seoul",                            // location
            100L,                               // linkedJobId
            false,                              // isFromJobComment
            Popularity.empty(),                 // popularity
            false,                              // isDeleted
            Instant.now(),                      // createdAt
            Instant.now()                       // updatedAt
    );

    // CommunityPostRead for read operations (with nickname)
    private static final CommunityPostRead sampleCommunityPostRead = new CommunityPostRead(
            1L,                                  // communityPostId
            1L,                                  // userId
            "testuser",                          // nickname
            CommunityPostCategory.INTERVIEW_SHARE, // category
            "Test Community Post Title",         // title
            "Test markdown body content",        // markdownBody
            "Test Company",                      // company
            "Seoul",                            // location
            100L,                               // linkedJobId
            false,                              // isFromJobComment
            Popularity.empty(),                 // popularity
            false,                              // isDeleted
            Instant.now(),                      // createdAt
            Instant.now()                       // updatedAt
    );

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(communityPostApiController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // JavaTimeModule 등록
    }

    @Test
    void getCommunityPost_existingId_returnsOkWithPost() throws Exception {
        // given
        when(communityPostReader.read(new CommunityPostIdentity(1L)))
                .thenReturn(sampleCommunityPostRead);

        // when & then - Status Code 검증
        MvcResult result = mockMvc.perform(get("/api/community-posts/{communityPostId}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        // then - Response Spec 검증
        String responseJson = result.getResponse().getContentAsString();
        CommunityPostResponse response = objectMapper.readValue(responseJson, CommunityPostResponse.class);

        assertThat(response.getCommunityPostId()).isEqualTo(1L);
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getNickname()).isEqualTo("testuser");
        assertThat(response.getCategory()).isEqualTo(CommunityPostCategory.INTERVIEW_SHARE);
        assertThat(response.getTitle()).isEqualTo("Test Community Post Title");
        assertThat(response.getMarkdownBody()).isEqualTo("Test markdown body content");
        assertThat(response.getCompany()).isEqualTo("Test Company");
        assertThat(response.getLocation()).isEqualTo("Seoul");

        verify(communityPostReader).read(new CommunityPostIdentity(1L));
    }

    @Test
    void getCommunityPost_nonExistingId_throwsException() throws Exception {
        // given
        when(communityPostReader.read(new CommunityPostIdentity(999L)))
                .thenThrow(new RuntimeException("Community post not found"));

        // when & then
        try {
            mockMvc.perform(get("/api/community-posts/{communityPostId}", 999L))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            // Controller에서 RuntimeException 발생 예상
            assertThat(e.getCause()).isInstanceOf(RuntimeException.class);
        }

        verify(communityPostReader).read(new CommunityPostIdentity(999L));
    }

    @Test
    void upsertCommunityPost_newPost_returnsCreatedWithPost() throws Exception {
        // given
        when(communityPostWriter.upsert(any(CommunityPost.class)))
                .thenReturn(sampleCommunityPost);
        when(communityPostReader.getById(any(CommunityPostIdentity.class)))
                .thenReturn(sampleCommunityPostRead);

        // when & then - Status Code 검증
        MvcResult result = mockMvc.perform(post("/api/community-posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"category\":\"INTERVIEW_SHARE\",\"title\":\"Test Community Post Title\",\"markdownBody\":\"Test markdown body content\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        // then - Response Spec 검증
        String responseJson = result.getResponse().getContentAsString();
        CommunityPostResponse response = objectMapper.readValue(responseJson, CommunityPostResponse.class);

        assertThat(response.getCommunityPostId()).isEqualTo(1L);
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getNickname()).isEqualTo("testuser");
        assertThat(response.getTitle()).isEqualTo("Test Community Post Title");
        assertThat(response.getCategory()).isEqualTo(CommunityPostCategory.INTERVIEW_SHARE);

        verify(communityPostWriter).upsert(any(CommunityPost.class));
        verify(communityPostReader).getById(any(CommunityPostIdentity.class));
    }

    @Test
    void upsertCommunityPost_existingPost_returnsCreatedWithUpdatedPost() throws Exception {
        // given
        CommunityPost updatedPost = new CommunityPost(
                1L, 1L, CommunityPostCategory.INTERVIEW_SHARE,
                "Updated Title", "Updated body",
                "Test Company", "Seoul", 100L, false,
                Popularity.empty(), false,
                Instant.now(), Instant.now()
        );
        CommunityPostRead updatedPostRead = new CommunityPostRead(
                1L, 1L, "testuser", CommunityPostCategory.INTERVIEW_SHARE,
                "Updated Title", "Updated body",
                "Test Company", "Seoul", 100L, false,
                Popularity.empty(), false,
                Instant.now(), Instant.now()
        );

        when(communityPostWriter.upsert(any(CommunityPost.class)))
                .thenReturn(updatedPost);
        when(communityPostReader.getById(any(CommunityPostIdentity.class)))
                .thenReturn(updatedPostRead);

        // when & then
        MvcResult result = mockMvc.perform(post("/api/community-posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"communityPostId\":1,\"userId\":1,\"category\":\"INTERVIEW_SHARE\",\"title\":\"Updated Title\",\"markdownBody\":\"Updated body\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        CommunityPostResponse response = objectMapper.readValue(responseJson, CommunityPostResponse.class);

        assertThat(response.getCommunityPostId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Updated Title");

        verify(communityPostWriter).upsert(any(CommunityPost.class));
        verify(communityPostReader).getById(any(CommunityPostIdentity.class));
    }

    @Test
    void deleteCommunityPost_existingId_returnsNoContent() throws Exception {
        // given
        doNothing().when(communityPostWriter).delete(new CommunityPostIdentity(1L));

        // when & then
        mockMvc.perform(delete("/api/community-posts/{communityPostId}", 1L))
                .andExpect(status().isNoContent());

        verify(communityPostWriter).delete(new CommunityPostIdentity(1L));
    }

    @Test
    void deleteCommunityPost_nonExistingId_throwsException() throws Exception {
        // given
        doThrow(new RuntimeException("Community post not found"))
                .when(communityPostWriter).delete(new CommunityPostIdentity(999L));

        // when & then
        try {
            mockMvc.perform(delete("/api/community-posts/{communityPostId}", 999L))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(RuntimeException.class);
        }

        verify(communityPostWriter).delete(new CommunityPostIdentity(999L));
    }
}
