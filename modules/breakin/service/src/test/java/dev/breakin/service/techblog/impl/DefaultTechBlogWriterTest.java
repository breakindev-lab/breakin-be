package dev.breakin.service.techblog.impl;

import dev.breakin.infra.techblog.repository.TechBlogRepository;
import dev.breakin.model.common.Popularity;
import dev.breakin.model.techblog.TechBlog;
import dev.breakin.model.techblog.TechBlogIdentity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultTechBlogWriterTest {

    @Mock
    private TechBlogRepository techBlogRepository;

    @InjectMocks
    private DefaultTechBlogWriter techBlogWriter;

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

    @Test
    void upsert_newBlog_callsRepositorySave() {
        // given
        TechBlog newBlog = TechBlog.newBlog(
            "https://example.com/blog/new",
            "new title",
            "new markdown body"
        );
        when(techBlogRepository.save(any(TechBlog.class)))
            .thenReturn(sampleBlog);

        // when
        TechBlog result = techBlogWriter.upsert(newBlog);

        // then
        assertNotNull(result);
        assertEquals(sampleBlog.getTechBlogId(), result.getTechBlogId());
        verify(techBlogRepository).save(newBlog);
    }

    @Test
    void upsert_existingBlog_callsRepositorySave() {
        // given
        TechBlog updatedBlog = new TechBlog(
            1L, "https://example.com/blog/1", "updated company", "updated title",
            "updated body", "https://example.com/updated.jpg",
            List.of("updated-tag"), List.of("Kotlin"),
            "https://original.com/updated", Popularity.empty(), false,
            Instant.now(), Instant.now()
        );
        when(techBlogRepository.save(any(TechBlog.class)))
            .thenReturn(updatedBlog);

        // when
        TechBlog result = techBlogWriter.upsert(updatedBlog);

        // then
        assertNotNull(result);
        assertEquals("updated company", result.getCompany());
        assertEquals("updated title", result.getTitle());
        verify(techBlogRepository).save(updatedBlog);
    }

    @Test
    void upsert_externalBlog_callsRepositorySave() {
        // given
        TechBlog externalBlog = TechBlog.newExternalBlog(
            "https://example.com/external",
            "external company",
            "external title",
            "external body",
            "https://original-external.com/post"
        );
        when(techBlogRepository.save(any(TechBlog.class)))
            .thenReturn(externalBlog);

        // when
        TechBlog result = techBlogWriter.upsert(externalBlog);

        // then
        assertNotNull(result);
        assertEquals("external company", result.getCompany());
        assertEquals("https://original-external.com/post", result.getOriginalUrl());
        verify(techBlogRepository).save(externalBlog);
    }

    @Test
    void upsert_blogWithTagsAndCategories_callsRepositorySave() {
        // given
        TechBlog blogWithMeta = new TechBlog(
            null, "https://example.com/meta", "meta company", "meta title",
            "meta body", null, List.of("tag1", "tag2", "tag3"),
            List.of("Java", "Spring", "Kotlin"), null,
            Popularity.empty(), false, Instant.now(), Instant.now()
        );
        when(techBlogRepository.save(any(TechBlog.class)))
            .thenReturn(blogWithMeta);

        // when
        TechBlog result = techBlogWriter.upsert(blogWithMeta);

        // then
        assertNotNull(result);
        assertEquals(3, result.getTags().size());
        assertEquals(3, result.getTechCategories().size());
        verify(techBlogRepository).save(blogWithMeta);
    }

    @Test
    void delete_existingBlog_callsRepositoryDelete() {
        // given
        TechBlogIdentity identity = new TechBlogIdentity(1L);
        doNothing().when(techBlogRepository).deleteById(identity);

        // when
        techBlogWriter.delete(identity);

        // then
        verify(techBlogRepository).deleteById(identity);
    }

    @Test
    void delete_callsRepositoryDeleteWithCorrectIdentity() {
        // given
        TechBlogIdentity identity = new TechBlogIdentity(999L);
        doNothing().when(techBlogRepository).deleteById(identity);

        // when
        techBlogWriter.delete(identity);

        // then
        verify(techBlogRepository).deleteById(identity);
    }
}
