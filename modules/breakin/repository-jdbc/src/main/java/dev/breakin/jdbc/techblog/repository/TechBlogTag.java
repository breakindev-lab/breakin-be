package dev.breakin.jdbc.techblog.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * TechBlog의 태그 정보를 저장하는 자식 엔티티
 * Spring Data JDBC가 자동으로 tech_blog_tags 테이블과 매핑
 */
@Table("tech_blog_tags")
@Getter
@AllArgsConstructor
public class TechBlogTag {
    @Column("tag_name")
    private String tagName;
}
