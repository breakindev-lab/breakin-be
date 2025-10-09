package dev.breakin.api.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.breakin.api.comment.dto.CommentResponse;
import dev.breakin.api.comment.dto.CommentUpdateRequest;
import dev.breakin.api.comment.dto.CommentWriteRequest;
import dev.breakin.model.comment.Comment;
import dev.breakin.model.comment.CommentIdentity;
import dev.breakin.model.comment.CommentRead;
import dev.breakin.model.common.CommentOrder;
import dev.breakin.model.common.TargetType;
import dev.breakin.service.comment.CommentReader;
import dev.breakin.service.comment.CommentWriter;
import dev.breakin.service.comment.dto.CommentWriteCommand;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CommentApiControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommentReader commentReader;

    @Mock
    private CommentWriter commentWriter;

    @InjectMocks
    private CommentApiController commentApiController;

    private ObjectMapper objectMapper;

    // Comment for write operations
    private static final Comment sampleComment = new Comment(
            1L,                      // commentId
            1L,                      // userId
            "Test comment content",  // content
            TargetType.JOB,         // targetType
            100L,                    // targetId
            null,                    // parentId
            CommentOrder.empty(),    // commentOrder
            false,                   // isHidden
            Instant.now(),           // createdAt
            Instant.now()            // updatedAt
    );

    private static final Comment sampleReply = new Comment(
            2L,                      // commentId
            1L,                      // userId
            "Test reply content",    // content
            TargetType.JOB,         // targetType
            100L,                    // targetId
            1L,                      // parentId
            CommentOrder.empty(),    // commentOrder
            false,                   // isHidden
            Instant.now(),           // createdAt
            Instant.now()            // updatedAt
    );

    // CommentRead for read operations (with nickname)
    private static final CommentRead sampleCommentRead = new CommentRead(
            1L,                      // commentId
            1L,                      // userId
            "testuser",              // nickname
            "Test comment content",  // content
            TargetType.JOB,         // targetType
            100L,                    // targetId
            null,                    // parentId
            CommentOrder.empty(),    // commentOrder
            false,                   // isHidden
            Instant.now(),           // createdAt
            Instant.now()            // updatedAt
    );

    private static final CommentRead sampleReplyRead = new CommentRead(
            2L,                      // commentId
            1L,                      // userId
            "testuser",              // nickname
            "Test reply content",    // content
            TargetType.JOB,         // targetType
            100L,                    // targetId
            1L,                      // parentId
            CommentOrder.empty(),    // commentOrder
            false,                   // isHidden
            Instant.now(),           // createdAt
            Instant.now()            // updatedAt
    );

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentApiController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // JavaTimeModule 등록
    }

    @Test
    void getCommentsByTarget_existingComments_returnsOkWithList() throws Exception {
        // given
        when(commentReader.getByTargetTypeAndTargetId(TargetType.JOB, 100L))
                .thenReturn(List.of(sampleCommentRead, sampleReplyRead));

        // when & then - Status Code 검증
        MvcResult result = mockMvc.perform(get("/api/comments")
                        .param("targetType", "JOB")
                        .param("targetId", "100"))
                .andExpect(status().isOk())
                .andReturn();

        // then - Response Spec 검증
        String responseJson = result.getResponse().getContentAsString();
        CommentResponse[] responses = objectMapper.readValue(responseJson, CommentResponse[].class);

        assertThat(responses).hasSize(2);
        assertThat(responses[0].getCommentId()).isEqualTo(1L);
        assertThat(responses[0].getNickname()).isEqualTo("testuser");
        assertThat(responses[0].getContent()).isEqualTo("Test comment content");
        assertThat(responses[1].getCommentId()).isEqualTo(2L);

        verify(commentReader).getByTargetTypeAndTargetId(TargetType.JOB, 100L);
    }

    @Test
    void getCommentsByTarget_noComments_returnsOkWithEmptyArray() throws Exception {
        // given
        when(commentReader.getByTargetTypeAndTargetId(TargetType.JOB, 999L))
                .thenReturn(List.of());

        // when & then
        MvcResult result = mockMvc.perform(get("/api/comments")
                        .param("targetType", "JOB")
                        .param("targetId", "999"))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        CommentResponse[] responses = objectMapper.readValue(responseJson, CommentResponse[].class);

        assertThat(responses).isEmpty();
        verify(commentReader).getByTargetTypeAndTargetId(TargetType.JOB, 999L);
    }

    @Test
    void writeComment_validRequest_returnsCreatedWithComment() throws Exception {
        // given
        CommentWriteRequest request = new CommentWriteRequest();
        // request 필드는 리플렉션으로 설정하거나 생성자가 있다면 사용

        when(commentWriter.write(any(CommentWriteCommand.class)))
                .thenReturn(sampleComment);
        when(commentReader.getById(any(CommentIdentity.class)))
                .thenReturn(sampleCommentRead);

        // when & then - Status Code 검증
        MvcResult result = mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"content\":\"Test comment content\",\"targetType\":\"JOB\",\"targetId\":100}"))
                .andExpect(status().isCreated())
                .andReturn();

        // then - Response Spec 검증
        String responseJson = result.getResponse().getContentAsString();
        CommentResponse response = objectMapper.readValue(responseJson, CommentResponse.class);

        assertThat(response.getCommentId()).isEqualTo(1L);
        assertThat(response.getUserId()).isEqualTo(1L);
        assertThat(response.getNickname()).isEqualTo("testuser");
        assertThat(response.getContent()).isEqualTo("Test comment content");
        assertThat(response.getTargetType()).isEqualTo(TargetType.JOB);
        assertThat(response.getTargetId()).isEqualTo(100L);

        verify(commentWriter).write(any(CommentWriteCommand.class));
        verify(commentReader).getById(any(CommentIdentity.class));
    }

    @Test
    void writeComment_withParentId_returnsCreatedWithReply() throws Exception {
        // given
        when(commentWriter.write(any(CommentWriteCommand.class)))
                .thenReturn(sampleReply);
        when(commentReader.getById(any(CommentIdentity.class)))
                .thenReturn(sampleReplyRead);

        // when & then
        MvcResult result = mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\":1,\"content\":\"Test reply content\",\"targetType\":\"JOB\",\"targetId\":100,\"parentId\":1}"))
                .andExpect(status().isCreated())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        CommentResponse response = objectMapper.readValue(responseJson, CommentResponse.class);

        assertThat(response.getCommentId()).isEqualTo(2L);
        assertThat(response.getParentId()).isEqualTo(1L);

        verify(commentWriter).write(any(CommentWriteCommand.class));
        verify(commentReader).getById(any(CommentIdentity.class));
    }

    @Test
    void updateComment_validRequest_returnsOkWithUpdatedComment() throws Exception {
        // given
        String newContent = "Updated comment content";
        Comment updatedComment = new Comment(
                1L, 1L, newContent, TargetType.JOB, 100L,
                null, CommentOrder.empty(), false,
                Instant.now(), Instant.now()
        );
        CommentRead updatedCommentRead = new CommentRead(
                1L, 1L, "testuser", newContent, TargetType.JOB, 100L,
                null, CommentOrder.empty(), false,
                Instant.now(), Instant.now()
        );

        when(commentWriter.updateComment(eq(1L), eq(newContent)))
                .thenReturn(updatedComment);
        when(commentReader.getById(any(CommentIdentity.class)))
                .thenReturn(updatedCommentRead);

        // when & then - Status Code 검증
        MvcResult result = mockMvc.perform(put("/api/comments/{commentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Updated comment content\"}"))
                .andExpect(status().isOk())
                .andReturn();

        // then - Response Spec 검증
        String responseJson = result.getResponse().getContentAsString();
        CommentResponse response = objectMapper.readValue(responseJson, CommentResponse.class);

        assertThat(response.getCommentId()).isEqualTo(1L);
        assertThat(response.getContent()).isEqualTo(newContent);

        verify(commentWriter).updateComment(1L, newContent);
        verify(commentReader).getById(any(CommentIdentity.class));
    }

    @Test
    void hideComment_existingComment_returnsOkWithHiddenComment() throws Exception {
        // given
        Comment hiddenComment = new Comment(
                1L, 1L, "Test comment content", TargetType.JOB, 100L,
                null, CommentOrder.empty(), true,
                Instant.now(), Instant.now()
        );
        CommentRead hiddenCommentRead = new CommentRead(
                1L, 1L, "testuser", "Test comment content", TargetType.JOB, 100L,
                null, CommentOrder.empty(), true,
                Instant.now(), Instant.now()
        );

        when(commentWriter.hideComment(1L))
                .thenReturn(hiddenComment);
        when(commentReader.getById(any(CommentIdentity.class)))
                .thenReturn(hiddenCommentRead);

        // when & then - Status Code 검증
        MvcResult result = mockMvc.perform(post("/api/comments/{commentId}/hide", 1L))
                .andExpect(status().isOk())
                .andReturn();

        // then - Response Spec 검증
        String responseJson = result.getResponse().getContentAsString();
        CommentResponse response = objectMapper.readValue(responseJson, CommentResponse.class);

        assertThat(response.getCommentId()).isEqualTo(1L);
        assertThat(response.getIsHidden()).isTrue();

        verify(commentWriter).hideComment(1L);
        verify(commentReader).getById(any(CommentIdentity.class));
    }

    @Test
    void showComment_hiddenComment_returnsOkWithShownComment() throws Exception {
        // given
        Comment shownComment = new Comment(
                1L, 1L, "Test comment content", TargetType.JOB, 100L,
                null, CommentOrder.empty(), false,
                Instant.now(), Instant.now()
        );
        CommentRead shownCommentRead = new CommentRead(
                1L, 1L, "testuser", "Test comment content", TargetType.JOB, 100L,
                null, CommentOrder.empty(), false,
                Instant.now(), Instant.now()
        );

        when(commentWriter.showComment(1L))
                .thenReturn(shownComment);
        when(commentReader.getById(any(CommentIdentity.class)))
                .thenReturn(shownCommentRead);

        // when & then - Status Code 검증
        MvcResult result = mockMvc.perform(post("/api/comments/{commentId}/show", 1L))
                .andExpect(status().isOk())
                .andReturn();

        // then - Response Spec 검증
        String responseJson = result.getResponse().getContentAsString();
        CommentResponse response = objectMapper.readValue(responseJson, CommentResponse.class);

        assertThat(response.getCommentId()).isEqualTo(1L);
        assertThat(response.getIsHidden()).isFalse();

        verify(commentWriter).showComment(1L);
        verify(commentReader).getById(any(CommentIdentity.class));
    }
}
