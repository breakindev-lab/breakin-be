package dev.breakin.service.communitypost.impl;

import dev.breakin.infra.communitypost.repository.CommunityPostRepository;
import dev.breakin.model.communitypost.CommunityPost;
import dev.breakin.model.communitypost.CommunityPostCategory;
import dev.breakin.model.communitypost.CommunityPostIdentity;
import dev.breakin.model.common.Popularity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultCommunityPostWriterTest {

    @Mock
    private CommunityPostRepository communityPostRepository;

    @InjectMocks
    private DefaultCommunityPostWriter communityPostWriter;

    private final CommunityPost samplePost = new CommunityPost(
        1L,                                  // communityPostId
        1L,                                  // userId
        CommunityPostCategory.INTERVIEW_SHARE, // category
        "test title",                        // title
        "test markdown body",                // markdownBody
        "test company",                      // company
        "test location",                     // location
        null,                                // linkedJobId
        false,                               // isFromJobComment
        Popularity.empty(),                  // popularity
        false,                               // isDeleted
        Instant.now(),                       // createdAt
        Instant.now()                        // updatedAt
    );

    @Test
    void upsert_newPost_callsRepositorySave() {
        // given
        CommunityPost newPost = CommunityPost.newPost(
            1L,
            CommunityPostCategory.INTERVIEW_SHARE,
            "new title",
            "new markdown body"
        );
        when(communityPostRepository.save(any(CommunityPost.class)))
            .thenReturn(samplePost);

        // when
        CommunityPost result = communityPostWriter.upsert(newPost);

        // then
        assertNotNull(result);
        assertEquals(samplePost.getCommunityPostId(), result.getCommunityPostId());
        verify(communityPostRepository).save(newPost);
    }

    @Test
    void upsert_existingPost_callsRepositorySave() {
        // given
        CommunityPost updatedPost = new CommunityPost(
            1L, 1L, CommunityPostCategory.INTERVIEW_SHARE,
            "updated title", "updated body",
            "test company", "test location", null, false,
            Popularity.empty(), false, Instant.now(), Instant.now()
        );
        when(communityPostRepository.save(any(CommunityPost.class)))
            .thenReturn(updatedPost);

        // when
        CommunityPost result = communityPostWriter.upsert(updatedPost);

        // then
        assertNotNull(result);
        assertEquals("updated title", result.getTitle());
        assertEquals("updated body", result.getMarkdownBody());
        verify(communityPostRepository).save(updatedPost);
    }

    @Test
    void upsert_postFromJobComment_callsRepositorySave() {
        // given
        CommunityPost fromJobComment = CommunityPost.fromJobComment(
            1L,
            "interview title",
            "interview body",
            100L,
            "job company",
            "job location"
        );
        when(communityPostRepository.save(any(CommunityPost.class)))
            .thenReturn(fromJobComment);

        // when
        CommunityPost result = communityPostWriter.upsert(fromJobComment);

        // then
        assertNotNull(result);
        assertTrue(result.getIsFromJobComment());
        assertEquals(CommunityPostCategory.INTERVIEW_SHARE, result.getCategory());
        verify(communityPostRepository).save(fromJobComment);
    }

    @Test
    void delete_existingPost_callsRepositoryDelete() {
        // given
        CommunityPostIdentity identity = new CommunityPostIdentity(1L);
        doNothing().when(communityPostRepository).deleteById(identity);

        // when
        communityPostWriter.delete(identity);

        // then
        verify(communityPostRepository).deleteById(identity);
    }

    @Test
    void delete_callsRepositoryDeleteWithCorrectIdentity() {
        // given
        CommunityPostIdentity identity = new CommunityPostIdentity(999L);
        doNothing().when(communityPostRepository).deleteById(identity);

        // when
        communityPostWriter.delete(identity);

        // then
        verify(communityPostRepository).deleteById(identity);
    }
}
