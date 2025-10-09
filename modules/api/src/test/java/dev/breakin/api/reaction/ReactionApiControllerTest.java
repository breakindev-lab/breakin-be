package dev.breakin.api.reaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.breakin.model.common.TargetType;
import dev.breakin.service.reaction.ReactionWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ReactionApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReactionWriter reactionWriter;

    @InjectMocks
    private ReactionApiController reactionApiController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reactionApiController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // JavaTimeModule 등록
    }

    @Test
    void likeUp_validRequest_returnsOk() throws Exception {
        // given
        doNothing().when(reactionWriter).likeUp(1L, TargetType.JOB, 100L);

        // when & then
        mockMvc.perform(post("/api/reactions/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"targetType\":\"JOB\",\"targetId\":100}"))
                .andExpect(status().isOk());

        verify(reactionWriter).likeUp(1L, TargetType.JOB, 100L);
    }

    @Test
    void likeUp_exception_throwsException() throws Exception {
        // given
        doThrow(new RuntimeException("Like failed"))
                .when(reactionWriter).likeUp(1L, TargetType.JOB, 100L);

        // when & then
        try {
            mockMvc.perform(post("/api/reactions/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"userId\":1,\"targetType\":\"JOB\",\"targetId\":100}"))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            // Expected exception
        }

        verify(reactionWriter).likeUp(1L, TargetType.JOB, 100L);
    }

    @Test
    void dislikeUp_validRequest_returnsOk() throws Exception {
        // given
        doNothing().when(reactionWriter).dislikeUp(1L, TargetType.JOB, 100L);

        // when & then
        mockMvc.perform(post("/api/reactions/dislike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"targetType\":\"JOB\",\"targetId\":100}"))
                .andExpect(status().isOk());

        verify(reactionWriter).dislikeUp(1L, TargetType.JOB, 100L);
    }

    @Test
    void dislikeUp_exception_throwsException() throws Exception {
        // given
        doThrow(new RuntimeException("Dislike failed"))
                .when(reactionWriter).dislikeUp(1L, TargetType.JOB, 100L);

        // when & then
        try {
            mockMvc.perform(post("/api/reactions/dislike")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"userId\":1,\"targetType\":\"JOB\",\"targetId\":100}"))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            // Expected exception
        }

        verify(reactionWriter).dislikeUp(1L, TargetType.JOB, 100L);
    }

    @Test
    void likeDown_validRequest_returnsNoContent() throws Exception {
        // given
        doNothing().when(reactionWriter).likeDown(1L, TargetType.JOB, 100L);

        // when & then
        mockMvc.perform(delete("/api/reactions/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"targetType\":\"JOB\",\"targetId\":100}"))
                .andExpect(status().isNoContent());

        verify(reactionWriter).likeDown(1L, TargetType.JOB, 100L);
    }

    @Test
    void likeDown_exception_throwsException() throws Exception {
        // given
        doThrow(new RuntimeException("Like removal failed"))
                .when(reactionWriter).likeDown(1L, TargetType.JOB, 100L);

        // when & then
        try {
            mockMvc.perform(delete("/api/reactions/like")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"userId\":1,\"targetType\":\"JOB\",\"targetId\":100}"))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            // Expected exception
        }

        verify(reactionWriter).likeDown(1L, TargetType.JOB, 100L);
    }

    @Test
    void dislikeDown_validRequest_returnsNoContent() throws Exception {
        // given
        doNothing().when(reactionWriter).dislikeDown(1L, TargetType.JOB, 100L);

        // when & then
        mockMvc.perform(delete("/api/reactions/dislike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"targetType\":\"JOB\",\"targetId\":100}"))
                .andExpect(status().isNoContent());

        verify(reactionWriter).dislikeDown(1L, TargetType.JOB, 100L);
    }

    @Test
    void dislikeDown_exception_throwsException() throws Exception {
        // given
        doThrow(new RuntimeException("Dislike removal failed"))
                .when(reactionWriter).dislikeDown(1L, TargetType.JOB, 100L);

        // when & then
        try {
            mockMvc.perform(delete("/api/reactions/dislike")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"userId\":1,\"targetType\":\"JOB\",\"targetId\":100}"))
                    .andExpect(status().is5xxServerError());
        } catch (Exception e) {
            // Expected exception
        }

        verify(reactionWriter).dislikeDown(1L, TargetType.JOB, 100L);
    }
}
