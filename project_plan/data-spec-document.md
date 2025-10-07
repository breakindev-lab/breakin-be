# BreakIn.Tech - Data Specifications

## 1. User (유저)

```javascript
User {
  // 기본 식별자
  id: string (UUID)
  
  // 구글 OAuth 정보
  google_id: string  // 구글 고유 ID
  email: string  // 구글 이메일 (인증됨)
  
  // 서비스 프로필
  nickname: string  // 익명 닉네임 (유저가 설정)
  
  // 권한
  user_role: string  // "USER" | "ADMIN"
  
  // 관심사
  interested_companies: string[]  // ["google", "meta"]
  interested_locations: string[]  // ["mountain-view", "seattle"]
  
  // 알림 설정
  notification_settings: {
    email_enabled: boolean
    new_job_alerts: boolean
    reply_alerts: boolean
    like_alerts: boolean
  }
  
  // 통계/메타
  post_count: number
  comment_count: number
  likes_received: number
  
  // 시스템
  created_at: datetime
  last_login_at: datetime
  is_active: boolean
}
```

---

## 2. Job (채용공고)

```javascript
Job {
  // 기본 식별자
  id: number (Long)
  
  // 원본 정보
  url: string  // 채용 공고 원본 URL
  
  // 회사/포지션 정보
  company: string  // "Google"
  title: string  // "Software Engineer - Infrastructure"
  organization: string  // 부서/팀명
  
  // 설명
  markdown_body: string  // 마크다운 형식 본문
  one_line_summary: string  // 한 줄 요약
  
  // 경력 정보
  min_years: number (nullable)
  max_years: number (nullable)
  experience_required: boolean
  career_level: string  // "JUNIOR" | "MID" | "SENIOR" | "LEAD"
  
  // 고용 정보
  employment_type: string  // "FULL_TIME" | "CONTRACT" | "INTERN"
  position_category: string  // "ENGINEERING" | "PRODUCT" | "DATA"
  remote_policy: string  // "REMOTE" | "HYBRID" | "ONSITE"
  tech_categories: string[]  // ["BACKEND", "INFRASTRUCTURE", "PYTHON"]
  
  // 채용 기간
  started_at: datetime
  ended_at: datetime (nullable)
  is_open_ended: boolean
  is_closed: boolean
  
  // 위치
  location: string  // "Mountain View, CA, US"
  
  // 전형 정보
  has_assignment: boolean  // 과제 여부
  has_coding_test: boolean  // 코딩테스트 여부
  has_live_coding: boolean  // 라이브코딩 여부
  interview_count: number (nullable)  // 면접 횟수
  interview_days: number (nullable)  // 전형 소요 일수
  
  // 인기도 (PopularityEmbeddable)
  popularity: {
    view_count: number
    comment_count: number
    like_count: number
    dislike_count: number
  }
  
  // 시스템
  is_deleted: boolean
}
```

---

## 3. CommunityPost (커뮤니티 글)

```javascript
CommunityPost {
  // 기본 식별자
  id: number (Long)
  
  // 작성자
  user_id: number
  
  // 카테고리
  category: string  // "INTERVIEW_SHARE" | "QUESTION" | "CHIT_CHAT" | "TECH_ARTICLE"
  
  // 내용
  title: string
  markdown_body: string  // 마크다운 형식
  
  // 회사/지역 (선택적)
  company: string (nullable)  // "Google"
  location: string (nullable)  // "Mountain View, CA"
  
  // 채용 공고 연결 (댓글에서 온 경우)
  linked_job_id: number (nullable)
  is_from_job_comment: boolean  // 채용공고 댓글에서 자동 생성된 글인지
  
  // 인기도 (PopularityEmbeddable - Job과 동일)
  popularity: {
    view_count: number
    comment_count: number
    like_count: number
    dislike_count: number
  }
  
  // 시스템
  created_at: datetime
  updated_at: datetime
  is_deleted: boolean
}
```

---

## 4. Comment (댓글)

```javascript
Comment {
  // 기본 식별자
  id: number (Long)
  
  // 작성자
  user_id: number
  
  // 내용
  content: string
  
  // 댓글 대상
  target_type: string  // "job" | "community_post" | "blog"
  target_id: number
  
  // 계층 구조
  parent_id: number (nullable)  // 대댓글용
  
  // 상태
  is_hidden: boolean
  
  // 시스템
  created_at: datetime
  updated_at: datetime
}
```

**특징:**
- `target_type`과 `target_id`로 Job, CommunityPost, Blog에 모두 사용 가능
- `parent_id`로 대댓글 계층 구조 지원

---

## 5. Notification (알림)

```javascript
Notification {
  // 기본 식별자
  id: number (Long)
  
  // 수신자
  user_id: number
  
  // 알림 타입
  type: string  // "COMMENT_REPLY" | "POST_LIKE" | "COMMENT_LIKE" | 
                // "NEW_JOB" | "MENTIONED" | "JOB_DEADLINE"
  
  // 알림 내용
  title: string  // "새 답글이 달렸습니다"
  content: string  // "tech_hunter_123님이 댓글에 답글을 달았습니다"
  
  // 연결 정보
  target_type: string  // "job" | "community_post" | "comment"
  target_id: number
  
  // 발생시킨 유저
  actor_user_id: number (nullable)  // 누가 이 행동을 했는지
  
  // 상태
  is_read: boolean
  
  // 시스템
  created_at: datetime
}
```

---

## 6. UserActivity (유저 활동 로그)

```javascript
UserActivity {
  // 기본 식별자
  id: number (Long)
  
  // 유저
  user_id: number
  
  // 활동 타입
  activity_type: string  // "POST" | "COMMENT" | "LIKE" | "BOOKMARK"
  
  // 대상
  target_type: string  // "job" | "community_post" | "comment" | "blog"
  target_id: number
  
  // 메타 정보 (optional - 빠른 조회용)
  target_title: string (nullable)  // 글/댓글 제목/내용 일부
  target_company: string (nullable)  // 회사명
  
  // 시스템
  created_at: datetime
}
```

**용도:**
- 마이페이지에서 유저 활동 히스토리 조회
- Like/Comment/Post 생성 시 자동으로 로그 추가
- 인덱스: (user_id, created_at)

---

## 7. Blog (테크 블로그)

```javascript
Blog {
  // 기본 식별자
  id: number (Long)
  
  // 작성자
  user_id: number
  
  // 회사
  company: string (nullable)  // "Google", "Meta" 등
  
  // 내용
  title: string
  markdown_body: string  // 마크다운 형식
  thumbnail_url: string (nullable)  // 썸네일 이미지
  
  // 분류
  tags: string[]  // ["React", "System Design", "Interview"]
  tech_categories: string[]  // ["FRONTEND", "BACKEND", "DEVOPS"]
  
  // 외부 링크 (optional)
  original_url: string (nullable)  // 원본 블로그 링크 (외부 글인 경우)
  
  // 인기도 (PopularityEmbeddable - 동일)
  popularity: {
    view_count: number
    comment_count: number
    like_count: number
    dislike_count: number
  }
  
  // 시스템
  created_at: datetime
  updated_at: datetime
  is_deleted: boolean
}
```

---

## 8. Like (좋아요)

```javascript
Like {
  // 기본 식별자
  id: number (Long)
  
  // 유저
  user_id: number
  
  // 좋아요 대상
  target_type: string  // "job" | "community_post" | "comment" | "blog"
  target_id: number
  
  // 시스템
  created_at: datetime
}
```

**제약 조건:**
- Unique Index: (user_id, target_type, target_id)
- 한 유저가 같은 대상에 중복 좋아요 방지

---

## 공통 패턴

### PopularityEmbeddable
Job, CommunityPost, Blog에서 공통으로 사용:
```javascript
{
  view_count: number
  comment_count: number
  like_count: number
  dislike_count: number
}
```

### Polymorphic Association
Comment, Like, UserActivity, Notification에서 사용:
- `target_type` + `target_id`로 다양한 엔티티 참조

---

## 데이터 흐름 예시

### 1. 채용공고 댓글 → 커뮤니티 게시글
```
1. User가 Job에 댓글 작성 (Comment 생성)
2. "Post to Community" 선택
3. CommunityPost 자동 생성
   - linked_job_id = job.id
   - is_from_job_comment = true
   - company, location 자동 설정
4. UserActivity 로그 추가 (activity_type: "POST")
```

### 2. 좋아요 누르기
```
1. User가 CommunityPost에 좋아요
2. Like 생성 (target_type: "community_post", target_id: post.id)
3. CommunityPost.popularity.like_count 증가
4. UserActivity 로그 추가 (activity_type: "LIKE")
5. Post 작성자에게 Notification 생성 (type: "POST_LIKE")
```

### 3. 댓글 달기
```
1. User가 Job에 댓글 작성
2. Comment 생성 (target_type: "job", target_id: job.id)
3. Job.popularity.comment_count 증가
4. UserActivity 로그 추가 (activity_type: "COMMENT")
5. 대댓글이면 parent 유저에게 Notification (type: "COMMENT_REPLY")
```

---

## 인덱스 전략 (참고)

### User
- `google_id` (unique)
- `email` (unique)

### Job
- `company`, `location` (필터링)
- `is_closed`, `is_deleted`

### CommunityPost
- `user_id`
- `category`
- `company`, `location`
- `linked_job_id`

### Comment
- `(target_type, target_id)` (복합)
- `user_id`
- `parent_id`

### Like
- `(user_id, target_type, target_id)` (unique 복합)
- `(target_type, target_id)` (카운팅용)

### UserActivity
- `(user_id, created_at)` (복합)

### Notification
- `(user_id, is_read)`

---

**Last Updated:** 2024-10-06