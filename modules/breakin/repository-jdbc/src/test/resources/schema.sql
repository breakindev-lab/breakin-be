/*
 * Copyright 2024 breakin Inc. - All Rights Reserved.
 * Test Schema for H2 Database
 */

-- ========================================
-- Users 테이블 생성
-- ========================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    google_id VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    nickname VARCHAR(100) NOT NULL UNIQUE,
    user_role VARCHAR(50) NOT NULL DEFAULT 'USER',

    -- NotificationSettings (Embedded)
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

CREATE INDEX IF NOT EXISTS idx_user_interested_companies_user_id ON user_interested_companies(user_id);

-- User 관심 지역 테이블 (1:N)
CREATE TABLE IF NOT EXISTS user_interested_locations (
    user_id BIGINT NOT NULL,
    location_name VARCHAR(255) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_user_interested_locations_user_id ON user_interested_locations(user_id);

-- ========================================
-- TechBlogs 테이블 생성
-- ========================================
CREATE TABLE IF NOT EXISTS tech_blogs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(512) NOT NULL UNIQUE,
    company VARCHAR(255),
    title VARCHAR(500) NOT NULL,
    markdown_body TEXT,
    thumbnail_url VARCHAR(512),
    original_url VARCHAR(512),

    -- Popularity (Embedded)
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

CREATE INDEX IF NOT EXISTS idx_tech_blog_tags_tech_blog_id ON tech_blog_tags(tech_blog_id);

-- TechBlog 기술 카테고리 테이블 (1:N)
CREATE TABLE IF NOT EXISTS tech_blog_tech_categories (
    tech_blog_id BIGINT NOT NULL,
    category_name VARCHAR(100) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_tech_blog_tech_categories_tech_blog_id ON tech_blog_tech_categories(tech_blog_id);

-- ========================================
-- Jobs 테이블 생성
-- ========================================
CREATE TABLE IF NOT EXISTS jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(512) NOT NULL UNIQUE,
    company VARCHAR(255) NOT NULL,
    title VARCHAR(500) NOT NULL,
    organization VARCHAR(255),
    one_line_summary VARCHAR(500),
    min_years INT,
    max_years INT,
    experience_required BOOLEAN NOT NULL DEFAULT FALSE,
    career_level VARCHAR(50) NOT NULL DEFAULT 'ENTRY',
    employment_type VARCHAR(50) NOT NULL DEFAULT 'FULL_TIME',
    position_category VARCHAR(100),
    remote_policy VARCHAR(50) NOT NULL DEFAULT 'ONSITE',
    started_at TIMESTAMP WITH TIME ZONE NOT NULL,
    ended_at TIMESTAMP WITH TIME ZONE,
    is_open_ended BOOLEAN NOT NULL DEFAULT FALSE,
    is_closed BOOLEAN NOT NULL DEFAULT FALSE,
    position_introduction TEXT,
    full_description TEXT,
    has_assignment BOOLEAN NOT NULL DEFAULT FALSE,
    has_coding_test BOOLEAN NOT NULL DEFAULT FALSE,
    has_live_coding BOOLEAN NOT NULL DEFAULT FALSE,
    interview_count INT,
    interview_days INT,

    -- JobCompensation (Embedded)
    min_base_pay DECIMAL(15, 2),
    max_base_pay DECIMAL(15, 2),
    currency VARCHAR(10),
    unit VARCHAR(20),
    has_stock_option BOOLEAN,
    salary_note VARCHAR(1000),

    -- Popularity (Embedded)
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

CREATE INDEX IF NOT EXISTS idx_job_tech_categories_job_id ON job_tech_categories(job_id);

-- Job 위치 테이블 (1:N)
CREATE TABLE IF NOT EXISTS job_locations (
    job_id BIGINT NOT NULL,
    location_name VARCHAR(255) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_job_locations_job_id ON job_locations(job_id);

-- Job 책임사항 테이블 (1:N)
CREATE TABLE IF NOT EXISTS job_responsibilities (
    job_id BIGINT NOT NULL,
    responsibility VARCHAR(500) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_job_responsibilities_job_id ON job_responsibilities(job_id);

-- Job 자격요건 테이블 (1:N)
CREATE TABLE IF NOT EXISTS job_qualifications (
    job_id BIGINT NOT NULL,
    qualification VARCHAR(500) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_job_qualifications_job_id ON job_qualifications(job_id);

-- Job 우대사항 테이블 (1:N)
CREATE TABLE IF NOT EXISTS job_preferred_qualifications (
    job_id BIGINT NOT NULL,
    preferred_qualification VARCHAR(500) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_job_preferred_qualifications_job_id ON job_preferred_qualifications(job_id);

-- ========================================
-- CommunityPosts 테이블 생성
-- ========================================
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

    -- Popularity (Embedded)
    view_count BIGINT NOT NULL DEFAULT 0,
    comment_count BIGINT NOT NULL DEFAULT 0,
    like_count BIGINT NOT NULL DEFAULT 0,
    dislike_count BIGINT NOT NULL DEFAULT 0,

    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- ========================================
-- Comments 테이블 생성
-- ========================================
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    target_id BIGINT NOT NULL,
    parent_id BIGINT,

    -- CommentOrder (Embedded)
    comment_order INT NOT NULL DEFAULT 0,
    level INT NOT NULL DEFAULT 0,
    sort_number INT NOT NULL DEFAULT 0,
    parent_id_order BIGINT,
    child_count INT NOT NULL DEFAULT 0,

    is_hidden BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_comments_target ON comments(target_type, target_id);
CREATE INDEX IF NOT EXISTS idx_comments_parent ON comments(parent_id);
CREATE INDEX IF NOT EXISTS idx_comments_order_sort ON comments(target_type, target_id, comment_order, sort_number);

-- ========================================
-- Reactions 테이블 생성
-- ========================================
CREATE TABLE IF NOT EXISTS reactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    target_id BIGINT NOT NULL,
    reaction_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_reactions_user_target ON reactions(user_id, target_type, target_id);
CREATE INDEX IF NOT EXISTS idx_reactions_target ON reactions(target_type, target_id);
CREATE INDEX IF NOT EXISTS idx_reactions_reaction_type ON reactions(target_type, target_id, reaction_type);

-- ========================================
-- Notifications 테이블 생성
-- ========================================
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    notification_type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    link_url VARCHAR(512),
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_notifications_user ON notifications(user_id);
CREATE INDEX IF NOT EXISTS idx_notifications_user_read ON notifications(user_id, is_read);

-- ========================================
-- UserActivities 테이블 생성
-- ========================================
CREATE TABLE IF NOT EXISTS user_activities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    activity_type VARCHAR(50) NOT NULL,
    target_type VARCHAR(50) NOT NULL,
    target_id BIGINT NOT NULL,
    metadata TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_user_activities_user ON user_activities(user_id);
CREATE INDEX IF NOT EXISTS idx_user_activities_target ON user_activities(target_type, target_id);
CREATE INDEX IF NOT EXISTS idx_user_activities_user_activity_type ON user_activities(user_id, activity_type);
