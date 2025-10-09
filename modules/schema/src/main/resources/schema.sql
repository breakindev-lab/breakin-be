/*
 * Copyright 2024 breakin Inc. - All Rights Reserved.
 */

-- ========================================
-- 테이블명 및 컬럼명 규칙
-- ========================================
-- 테이블명: 소문자 + 복수형 (예: users, orders, products)
-- 컬럼명: 스네이크케이스 소문자 (예: user_id, created_at)
-- 이유:
--   - Spring Data JDBC 기본 네이밍 전략과 일치
--   - PostgreSQL, MySQL 등 프로덕션 DB 마이그레이션 용이
--   - 대소문자 혼용으로 인한 매핑 이슈 방지
-- ========================================

-- Examples 테이블 생성
CREATE TABLE IF NOT EXISTS examples (
    example_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Users 테이블 생성
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    google_id VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    nickname VARCHAR(100) NOT NULL UNIQUE,
    user_role VARCHAR(50) NOT NULL DEFAULT 'USER',

    -- NotificationSettings (Embedded) - 컬럼으로 flatten
    email_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    new_job_alerts BOOLEAN NOT NULL DEFAULT TRUE,
    reply_alerts BOOLEAN NOT NULL DEFAULT TRUE,
    like_alerts BOOLEAN NOT NULL DEFAULT TRUE,

    -- 활동 통계
    post_count BIGINT NOT NULL DEFAULT 0,
    comment_count BIGINT NOT NULL DEFAULT 0,
    likes_received BIGINT NOT NULL DEFAULT 0,

    -- 메타 정보
    last_login_at TIMESTAMP WITH TIME ZONE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_withdrawn BOOLEAN NOT NULL DEFAULT FALSE,
    withdrawn_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- User 관심 회사 테이블 (1:N)
CREATE TABLE IF NOT EXISTS user_interested_companies (
    user_id BIGINT NOT NULL,
    company_name VARCHAR(255) NOT NULL
);
-- CREATE INDEX IF NOT EXISTS idx_user_interested_companies_user_id ON user_interested_companies(user_id);

-- User 관심 지역 테이블 (1:N)
CREATE TABLE IF NOT EXISTS user_interested_locations (
    user_id BIGINT NOT NULL,
    location_name VARCHAR(255) NOT NULL
);
-- CREATE INDEX IF NOT EXISTS idx_user_interested_locations_user_id ON user_interested_locations(user_id);

-- TechBlogs 테이블 생성
CREATE TABLE IF NOT EXISTS tech_blogs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(512) NOT NULL UNIQUE,
    company VARCHAR(255),
    title VARCHAR(500) NOT NULL,
    markdown_body TEXT,
    thumbnail_url VARCHAR(512),
    original_url VARCHAR(512),

    -- Popularity (Embedded) - 컬럼으로 flatten
    view_count BIGINT NOT NULL DEFAULT 0,
    comment_count BIGINT NOT NULL DEFAULT 0,
    like_count BIGINT NOT NULL DEFAULT 0,
    dislike_count BIGINT NOT NULL DEFAULT 0,

    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- TechBlog 태그 테이블 (1:N)
CREATE TABLE IF NOT EXISTS tech_blog_tags (
    tech_blog_id BIGINT NOT NULL,
    tag_name VARCHAR(100) NOT NULL
);
-- CREATE INDEX IF NOT EXISTS idx_tech_blog_tags_tech_blog_id ON tech_blog_tags(tech_blog_id);

-- TechBlog 기술 카테고리 테이블 (1:N)
CREATE TABLE IF NOT EXISTS tech_blog_tech_categories (
    tech_blog_id BIGINT NOT NULL,
    category_name VARCHAR(100) NOT NULL
);
-- CREATE INDEX IF NOT EXISTS idx_tech_blog_tech_categories_tech_blog_id ON tech_blog_tech_categories(tech_blog_id);

-- Jobs 테이블 생성
CREATE TABLE IF NOT EXISTS jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(512) NOT NULL UNIQUE,
    company VARCHAR(255) NOT NULL,
    title VARCHAR(500) NOT NULL,
    organization VARCHAR(255),
    markdown_body TEXT,
    one_line_summary VARCHAR(500),
    min_years INT,
    max_years INT,
    experience_required BOOLEAN NOT NULL DEFAULT FALSE,
    career_level VARCHAR(50) NOT NULL DEFAULT 'ENTRY',
    employment_type VARCHAR(50) NOT NULL DEFAULT 'FULL_TIME',
    position_category VARCHAR(100),
    remote_policy VARCHAR(50) NOT NULL DEFAULT 'ONSITE',
    started_at TIMESTAMP NOT NULL,
    ended_at TIMESTAMP,
    is_open_ended BOOLEAN NOT NULL DEFAULT FALSE,
    is_closed BOOLEAN NOT NULL DEFAULT FALSE,
    location VARCHAR(255) NOT NULL,
    has_assignment BOOLEAN NOT NULL DEFAULT FALSE,
    has_coding_test BOOLEAN NOT NULL DEFAULT FALSE,
    has_live_coding BOOLEAN NOT NULL DEFAULT FALSE,
    interview_count INT,
    interview_days INT,

    -- Popularity (Embedded) - 컬럼으로 flatten
    view_count BIGINT NOT NULL DEFAULT 0,
    comment_count BIGINT NOT NULL DEFAULT 0,
    like_count BIGINT NOT NULL DEFAULT 0,
    dislike_count BIGINT NOT NULL DEFAULT 0,

    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Job 기술 카테고리 테이블 (1:N)
CREATE TABLE IF NOT EXISTS job_tech_categories (
    job_id BIGINT NOT NULL,
    category_name VARCHAR(100) NOT NULL
);
-- CREATE INDEX IF NOT EXISTS idx_job_tech_categories_job_id ON job_tech_categories(job_id);

-- CommunityPosts 테이블 생성
CREATE TABLE IF NOT EXISTS community_posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category VARCHAR(100) NOT NULL,
    title VARCHAR(500) NOT NULL,
    markdown_body TEXT NOT NULL,
    company VARCHAR(255),
    location VARCHAR(255),
    linked_job_id BIGINT,
    is_from_job_comment BOOLEAN NOT NULL DEFAULT FALSE,

    -- Popularity (Embedded) - 컬럼으로 flatten
    view_count BIGINT NOT NULL DEFAULT 0,
    comment_count BIGINT NOT NULL DEFAULT 0,
    like_count BIGINT NOT NULL DEFAULT 0,
    dislike_count BIGINT NOT NULL DEFAULT 0,

    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Comments 테이블 생성
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    target_id BIGINT NOT NULL,
    parent_id BIGINT,

    -- CommentOrder (Embedded) - 계층 구조 관리
    comment_order INT NOT NULL DEFAULT 0,
    level INT NOT NULL DEFAULT 0,
    sort_number INT NOT NULL DEFAULT 0,
    child_count INT NOT NULL DEFAULT 0,

    is_hidden BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
-- CREATE INDEX IF NOT EXISTS idx_comments_target ON comments(target_type, target_id);
-- CREATE INDEX IF NOT EXISTS idx_comments_parent ON comments(parent_id);
-- CREATE INDEX IF NOT EXISTS idx_comments_order_sort ON comments(target_type, target_id, comment_order, sort_number);

-- Reactions 테이블 생성 (좋아요/싫어요)
CREATE TABLE IF NOT EXISTS reactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    target_id BIGINT NOT NULL,
    reaction_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
-- 중복 반응 방지를 위한 유니크 인덱스
-- CREATE UNIQUE INDEX IF NOT EXISTS uk_reactions_user_target ON reactions(user_id, target_type, target_id);
-- CREATE INDEX IF NOT EXISTS idx_reactions_target ON reactions(target_type, target_id);
-- CREATE INDEX IF NOT EXISTS idx_reactions_reaction_type ON reactions(target_type, target_id, reaction_type);

-- ========================================
-- 외래키 정책
-- ========================================
-- 외래키 제약 조건(FOREIGN KEY) 사용 안함
-- 다른 테이블 참조가 필요한 경우:
--   - 제약 조건 없이 ID 컬럼만 추가 (예: USER_ID BIGINT)
--   - 참조 무결성은 애플리케이션 레벨에서 관리
--   - 장점: 유연한 데이터 관리, 순환 참조 방지, 테스트 용이성
--
-- 예시:
-- ❌ 외래키 제약 사용 (사용하지 않음)
-- CREATE TABLE ORDERS (
--     ORDER_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
--     USER_ID BIGINT NOT NULL,
--     FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)  -- 사용 안함
-- );
--
-- ✅ ID만 포함 (권장)
-- CREATE TABLE ORDERS (
--     ORDER_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
--     USER_ID BIGINT NOT NULL,  -- 제약 조건 없이 ID만 저장
--     CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     UPDATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
-- );
