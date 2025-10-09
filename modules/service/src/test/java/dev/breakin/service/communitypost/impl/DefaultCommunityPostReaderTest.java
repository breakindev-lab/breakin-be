package dev.breakin.service.communitypost.impl;

import dev.breakin.infra.communitypost.repository.CommunityPostRepository;
import dev.breakin.model.communitypost.CommunityPost;
import dev.breakin.model.communitypost.CommunityPostCategory;
import dev.breakin.model.communitypost.CommunityPostIdentity;
import dev.breakin.model.communitypost.CommunityPostRead;
import dev.breakin.model.common.Popularity;
import dev.breakin.service.communitypost.view.CommunityPostViewMemory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultCommunityPostReaderTest {

    @Mock
    private CommunityPostRepository communityPostRepository;

    @Mock
    private CommunityPostViewMemory communityPostViewMemory;

    @InjectMocks
    private DefaultCommunityPostReader communityPostReader;

    // CommunityPostRead for read operations (with nickname)
    private static final CommunityPostRead samplePostRead = new CommunityPostRead(
        1L,                                  // communityPostId
        1L,                                  // userId
        "testuser",                          // nickname
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

    private final CommunityPostIdentity testIdentity = new CommunityPostIdentity(1L);

    @Test
    void read_existingId_returnsPostAndIncrementsViewCount() {
        // given
        when(communityPostRepository.findById(testIdentity))
            .thenReturn(Optional.of(samplePostRead));

        // when
        CommunityPostRead result = communityPostReader.read(testIdentity);

        // then
        assertNotNull(result);
        assertEquals(samplePostRead.getCommunityPostId(), result.getCommunityPostId());
        assertEquals(samplePostRead.getTitle(), result.getTitle());
        assertEquals(samplePostRead.getNickname(), result.getNickname());
        verify(communityPostRepository).findById(testIdentity);
        verify(communityPostViewMemory).countUp(1L);
    }

    @Test
    void read_nonExistingId_throwsException() {
        // given
        when(communityPostRepository.findById(testIdentity))
            .thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () ->
            communityPostReader.read(testIdentity)
        );
        verify(communityPostRepository).findById(testIdentity);
        verify(communityPostViewMemory, never()).countUp(any());
    }

    @Test
    void getById_existingId_returnsPostWithoutViewCount() {
        // given
        when(communityPostRepository.findById(testIdentity))
            .thenReturn(Optional.of(samplePostRead));

        // when
        CommunityPostRead result = communityPostReader.getById(testIdentity);

        // then
        assertNotNull(result);
        assertEquals(samplePostRead.getCommunityPostId(), result.getCommunityPostId());
        assertEquals(samplePostRead.getTitle(), result.getTitle());
        verify(communityPostRepository).findById(testIdentity);
        verify(communityPostViewMemory, never()).countUp(any());
    }

    @Test
    void getById_nonExistingId_throwsException() {
        // given
        when(communityPostRepository.findById(testIdentity))
            .thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () ->
            communityPostReader.getById(testIdentity)
        );
        verify(communityPostRepository).findById(testIdentity);
    }

    @Test
    void getAll_withData_returnsList() {
        // given
        List<CommunityPostRead> posts = List.of(samplePostRead);
        when(communityPostRepository.findAll())
            .thenReturn(posts);

        // when
        List<CommunityPostRead> result = communityPostReader.getAll();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(samplePostRead.getCommunityPostId(), result.get(0).getCommunityPostId());
        verify(communityPostRepository).findAll();
    }

    @Test
    void getAll_emptyData_returnsEmptyList() {
        // given
        when(communityPostRepository.findAll())
            .thenReturn(List.of());

        // when
        List<CommunityPostRead> result = communityPostReader.getAll();

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(communityPostRepository).findAll();
    }

    @Test
    void getByUserId_existingUser_returnsList() {
        // given
        Long userId = 1L;
        List<CommunityPostRead> posts = List.of(samplePostRead);
        when(communityPostRepository.findByUserId(userId))
            .thenReturn(posts);

        // when
        List<CommunityPostRead> result = communityPostReader.getByUserId(userId);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUserId());
        verify(communityPostRepository).findByUserId(userId);
    }

    @Test
    void getByUserId_nonExistingUser_returnsEmptyList() {
        // given
        Long userId = 999L;
        when(communityPostRepository.findByUserId(userId))
            .thenReturn(List.of());

        // when
        List<CommunityPostRead> result = communityPostReader.getByUserId(userId);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(communityPostRepository).findByUserId(userId);
    }

    @Test
    void getByCompany_existingCompany_returnsList() {
        // given
        String company = "test company";
        List<CommunityPostRead> posts = List.of(samplePostRead);
        when(communityPostRepository.findByCompany(company))
            .thenReturn(posts);

        // when
        List<CommunityPostRead> result = communityPostReader.getByCompany(company);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(company, result.get(0).getCompany());
        verify(communityPostRepository).findByCompany(company);
    }

    @Test
    void getByCompany_nonExistingCompany_returnsEmptyList() {
        // given
        String company = "nonexistent company";
        when(communityPostRepository.findByCompany(company))
            .thenReturn(List.of());

        // when
        List<CommunityPostRead> result = communityPostReader.getByCompany(company);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(communityPostRepository).findByCompany(company);
    }

    @Test
    void getByLocation_existingLocation_returnsList() {
        // given
        String location = "test location";
        List<CommunityPostRead> posts = List.of(samplePostRead);
        when(communityPostRepository.findByLocation(location))
            .thenReturn(posts);

        // when
        List<CommunityPostRead> result = communityPostReader.getByLocation(location);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(location, result.get(0).getLocation());
        verify(communityPostRepository).findByLocation(location);
    }

    @Test
    void getByLocation_nonExistingLocation_returnsEmptyList() {
        // given
        String location = "nonexistent location";
        when(communityPostRepository.findByLocation(location))
            .thenReturn(List.of());

        // when
        List<CommunityPostRead> result = communityPostReader.getByLocation(location);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(communityPostRepository).findByLocation(location);
    }
}
