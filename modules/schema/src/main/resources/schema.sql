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
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

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
