package dev.breakin.api.techblog;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.breakin.api.techblog.dto.TechBlogResponse;
import dev.breakin.model.common.Popularity;
import dev.breakin.model.techblog.TechBlog;
import dev.breakin.model.techblog.TechBlogIdentity;
import dev.breakin.service.techblog.TechBlogReader;
import dev.breakin.service.techblog.TechBlogWriter;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TechBlogApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TechBlogReader techBlogReader;

    @Mock
    private TechBlogWriter techBlogWriter;

    @InjectMocks
    private TechBlogApiController techBlogApiController;

    private ObjectMapper objectMapper;

    private final TechBlog sampleTechBlog = new TechBlog(
            1L,                                  // techBlogId
            "https://test.com/blog/1",          // url
            "Test Company",                     // company
            "Mastering Spring Boot",            // title
            "Test markdown body content",       // markdownBody
            "https://test.com/thumb.jpg",       // thumbnailUrl
            List.of("spring", "java"),          // tags
            List.of("backend", "framework"),    // techCategories
            "https://original.com/blog",        // originalUrl
            Popularity.empty(),                 // popularity
            false,                              // isDeleted
            Instant.now(),                      // createdAt
            Instant.now()                       // updatedAt
    );

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(techBlogApiController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // JavaTimeModule 등록
    }

    @Test
    void getTechBlog_existingId_returnsOkWithTechBlog() throws Exception {
        // given
        when(techBlogReader.read(new TechBlogIdentity(1L)))
                .thenReturn(sampleTechBlog);

        // when & then - Status Code 검증
        MvcResult result = mockMvc.perform(get("/api/tech-blogs/{techBlogId}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        // then - Response Spec 검증
        String responseJson = result.getResponse().getContentAsString();
        TechBlogResponse response = objectMapper.readValue(responseJson, TechBlogResponse.class);

        assertThat(response.getTechBlogId()).isEqualTo(1L);
        assertThat(response.getCompany()).isEqualTo("Test Company");
        assertThat(response.getTitle()).isEqualTo("Mastering Spring Boot");
        assertThat(response.getUrl()).isEqualTo("https://test.com/blog/1");
        assertThat(response.getTags()).containsExactly("spring", "java");

        verify(techBlogReader).read(new TechBlogIdentity(1L));
    }

    @Test
    void getTechBlog_nonExistingId_throwsException() throws Exception {
        // given
        when(techBlogReader.read(new TechBlogIdentity(999L)))
                .thenThrow(new RuntimeException("TechBlog not found"));

        // when & then
        try {
            mockMvc.perform(get("/api/tech-blogs/{techBlogId}", 999L))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            // Controller에서 RuntimeException 발생 예상
            assertThat(e.getCause()).isInstanceOf(RuntimeException.class);
        }

        verify(techBlogReader).read(new TechBlogIdentity(999L));
    }

    @Test
    void upsertTechBlog_newBlog_returnsCreatedWithTechBlog() throws Exception {
        // given
        when(techBlogWriter.upsert(any(TechBlog.class)))
                .thenReturn(sampleTechBlog);

        // when & then - Status Code 검증
        MvcResult result = mockMvc.perform(post("/api/tech-blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"url\":\"https://test.com/blog/1\",\"title\":\"Mastering Spring Boot\",\"markdownBody\":\"Test markdown body content\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        // then - Response Spec 검증
        String responseJson = result.getResponse().getContentAsString();
        TechBlogResponse response = objectMapper.readValue(responseJson, TechBlogResponse.class);

        assertThat(response.getTechBlogId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Mastering Spring Boot");
        assertThat(response.getUrl()).isEqualTo("https://test.com/blog/1");

        verify(techBlogWriter).upsert(any(TechBlog.class));
    }

    @Test
    void upsertTechBlog_existingBlog_returnsCreatedWithUpdatedBlog() throws Exception {
        // given
        TechBlog updatedBlog = new TechBlog(
                1L, "https://test.com/blog/1", "Test Company", "Updated Title",
                "Updated body", "https://test.com/thumb.jpg",
                List.of("spring", "java"), List.of("backend"),
                "https://original.com/blog", Popularity.empty(), false,
                Instant.now(), Instant.now()
        );

        when(techBlogWriter.upsert(any(TechBlog.class)))
                .thenReturn(updatedBlog);

        // when & then
        MvcResult result = mockMvc.perform(post("/api/tech-blogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"techBlogId\":1,\"url\":\"https://test.com/blog/1\",\"title\":\"Updated Title\",\"markdownBody\":\"Updated body\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        TechBlogResponse response = objectMapper.readValue(responseJson, TechBlogResponse.class);

        assertThat(response.getTechBlogId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Updated Title");

        verify(techBlogWriter).upsert(any(TechBlog.class));
    }

    @Test
    void deleteTechBlog_existingId_returnsNoContent() throws Exception {
        // given
        doNothing().when(techBlogWriter).delete(new TechBlogIdentity(1L));

        // when & then
        mockMvc.perform(delete("/api/tech-blogs/{techBlogId}", 1L))
                .andExpect(status().isNoContent());

        verify(techBlogWriter).delete(new TechBlogIdentity(1L));
    }

    @Test
    void deleteTechBlog_nonExistingId_throwsException() throws Exception {
        // given
        doThrow(new RuntimeException("TechBlog not found"))
                .when(techBlogWriter).delete(new TechBlogIdentity(999L));

        // when & then
        try {
            mockMvc.perform(delete("/api/tech-blogs/{techBlogId}", 999L))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            assertThat(e.getCause()).isInstanceOf(RuntimeException.class);
        }

        verify(techBlogWriter).delete(new TechBlogIdentity(999L));
    }
}
