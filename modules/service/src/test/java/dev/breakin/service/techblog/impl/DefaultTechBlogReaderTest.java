package dev.breakin.service.techblog.impl;

import dev.breakin.infra.techblog.repository.TechBlogRepository;
import dev.breakin.model.common.Popularity;
import dev.breakin.model.techblog.TechBlog;
import dev.breakin.model.techblog.TechBlogIdentity;
import dev.breakin.service.techblog.view.TechBlogViewMemory;
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
class DefaultTechBlogReaderTest {

    @Mock
    private TechBlogRepository techBlogRepository;

    @Mock
    private TechBlogViewMemory techBlogViewMemory;

    @InjectMocks
    private DefaultTechBlogReader techBlogReader;

    private final TechBlog sampleBlog = new TechBlog(
        1L,                              // techBlogId
        "https://example.com/blog/1",    // url
        "test company",                  // company
        "test title",                    // title
        "test markdown body",            // markdownBody
        "https://example.com/thumb.jpg", // thumbnailUrl
        List.of("tag1", "tag2"),         // tags
        List.of("Java", "Spring"),       // techCategories
        "https://original.com/post",     // originalUrl
        Popularity.empty(),              // popularity
        false,                           // isDeleted
        Instant.now(),                   // createdAt
        Instant.now()                    // updatedAt
    );

    private final TechBlogIdentity testIdentity = new TechBlogIdentity(1L);

    @Test
    void read_existingId_returnsBlogAndIncrementsViewCount() {
        // given
        when(techBlogRepository.findById(testIdentity))
            .thenReturn(Optional.of(sampleBlog));

        // when
        TechBlog result = techBlogReader.read(testIdentity);

        // then
        assertNotNull(result);
        assertEquals(sampleBlog.getTechBlogId(), result.getTechBlogId());
        assertEquals(sampleBlog.getTitle(), result.getTitle());
        verify(techBlogRepository).findById(testIdentity);
        verify(techBlogViewMemory).countUp(1L);
    }

    @Test
    void read_nonExistingId_throwsException() {
        // given
        when(techBlogRepository.findById(testIdentity))
            .thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () ->
            techBlogReader.read(testIdentity)
        );
        verify(techBlogRepository).findById(testIdentity);
        verify(techBlogViewMemory, never()).countUp(any());
    }

    @Test
    void getById_existingId_returnsBlogWithoutViewCount() {
        // given
        when(techBlogRepository.findById(testIdentity))
            .thenReturn(Optional.of(sampleBlog));

        // when
        TechBlog result = techBlogReader.getById(testIdentity);

        // then
        assertNotNull(result);
        assertEquals(sampleBlog.getTechBlogId(), result.getTechBlogId());
        assertEquals(sampleBlog.getTitle(), result.getTitle());
        verify(techBlogRepository).findById(testIdentity);
        verify(techBlogViewMemory, never()).countUp(any());
    }

    @Test
    void getById_nonExistingId_throwsException() {
        // given
        when(techBlogRepository.findById(testIdentity))
            .thenReturn(Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () ->
            techBlogReader.getById(testIdentity)
        );
        verify(techBlogRepository).findById(testIdentity);
    }

    @Test
    void getAll_withData_returnsList() {
        // given
        List<TechBlog> blogs = List.of(sampleBlog);
        when(techBlogRepository.findAll())
            .thenReturn(blogs);

        // when
        List<TechBlog> result = techBlogReader.getAll();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sampleBlog.getTechBlogId(), result.get(0).getTechBlogId());
        verify(techBlogRepository).findAll();
    }

    @Test
    void getAll_emptyData_returnsEmptyList() {
        // given
        when(techBlogRepository.findAll())
            .thenReturn(List.of());

        // when
        List<TechBlog> result = techBlogReader.getAll();

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(techBlogRepository).findAll();
    }

    @Test
    void findByUrl_existingUrl_returnsOptionalWithBlog() {
        // given
        String url = "https://example.com/blog/1";
        when(techBlogRepository.findByUrl(url))
            .thenReturn(Optional.of(sampleBlog));

        // when
        Optional<TechBlog> result = techBlogReader.findByUrl(url);

        // then
        assertTrue(result.isPresent());
        assertEquals(url, result.get().getUrl());
        verify(techBlogRepository).findByUrl(url);
    }

    @Test
    void findByUrl_nonExistingUrl_returnsEmptyOptional() {
        // given
        String url = "https://example.com/nonexistent";
        when(techBlogRepository.findByUrl(url))
            .thenReturn(Optional.empty());

        // when
        Optional<TechBlog> result = techBlogReader.findByUrl(url);

        // then
        assertFalse(result.isPresent());
        verify(techBlogRepository).findByUrl(url);
    }

    @Test
    void getByCompany_existingCompany_returnsList() {
        // given
        String company = "test company";
        List<TechBlog> blogs = List.of(sampleBlog);
        when(techBlogRepository.findByCompany(company))
            .thenReturn(blogs);

        // when
        List<TechBlog> result = techBlogReader.getByCompany(company);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(company, result.get(0).getCompany());
        verify(techBlogRepository).findByCompany(company);
    }

    @Test
    void getByCompany_nonExistingCompany_returnsEmptyList() {
        // given
        String company = "nonexistent company";
        when(techBlogRepository.findByCompany(company))
            .thenReturn(List.of());

        // when
        List<TechBlog> result = techBlogReader.getByCompany(company);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(techBlogRepository).findByCompany(company);
    }
}
