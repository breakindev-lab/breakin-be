package dev.breakin.service.techblog.impl;

import dev.breakin.model.techblog.TechBlog;
import dev.breakin.model.techblog.TechBlogIdentity;
import dev.breakin.service.techblog.TechBlogWriter;
import dev.breakin.infra.techblog.repository.TechBlogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * TechBlog 도메인 변경 서비스 구현체
 *
 * CQRS 패턴의 Command 책임을 구현하며,
 * Infrastructure Repository를 활용한 변경 로직을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultTechBlogWriter implements TechBlogWriter {

    private final TechBlogRepository techBlogRepository;

    @Override
    public TechBlog upsert(TechBlog techBlog) {
        log.info("Upserting TechBlog: {}", techBlog.getTechBlogId());
        TechBlog saved = techBlogRepository.save(techBlog);
        log.info("TechBlog upserted successfully: {}", saved.getTechBlogId());
        return saved;
    }

    @Override
    public void delete(TechBlogIdentity identity) {
        log.info("Deleting TechBlog by id: {}", identity.getTechBlogId());
        techBlogRepository.deleteById(identity);
        log.info("TechBlog deleted successfully: {}", identity.getTechBlogId());
    }
}
