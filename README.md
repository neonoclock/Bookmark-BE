# 📚 Bookmark — 책 리뷰 공유 서비스 (Backend)

**Bookmark** 는 읽은 책을 기록하고, 다른 독자들과 리뷰를 나누는 웹 서비스입니다.  
이 레포지토리는 **백엔드 전체**를 Spring Boot 기반으로 구현한 프로젝트로,  
Vanilla JS 기반의 Frontend 애플리케이션과 REST API로 연동됩니다.

---

## 🎬 데모 영상

프로젝트 동작 화면은 아래 영상을 통해 확인하실 수 있습니다.

[유튜브에서 보기](https://youtu.be/l-ZnRPHH6_U)

---

## ✨ 주요 기능 (Backend)

### ✅ 1. 사용자 인증 · 인가 (JWT 기반)

- Spring Security + **JWT 토큰 기반 인증 구조**
- Access Token + Refresh Token 발급 및 로테이션 방식 재발급
- `JwtTokenProvider` 로 토큰 서명/검증/클레임 추출
- `JwtAuthenticationFilter` 로 요청마다 Authorization 헤더에서 토큰 검증
- Refresh Token은 DB에 저장하여 탈취·로그아웃 대응 가능
- `/api/v1/users/login` → 이메일·비밀번호 인증 후 토큰 발급
- `/api/v1/users/refresh` → Refresh Token 기반 Access Token 재발급
- `/api/v1/users/me` → 인증된 사용자 정보 조회
- 프로필/비밀번호 변경, 회원 탈퇴 등 민감 기능은  
  `@AuthenticationPrincipal UserPrincipal` 로 현재 사용자 정보 확인 후 수행

---

### ✅ 2. 회원 관리(User)

#### ▶ 회원가입 (`POST /api/v1/users`)

- 이메일 중복 체크
- 비밀번호 BCrypt 암호화 저장
- 기본 권한 `USER` 부여
- Base64 프로필 이미지 저장 가능

#### ▶ 로그인 (`POST /api/v1/users/login`)

- AuthenticationManager 기반 이메일·비밀번호 인증
- Access Token + Refresh Token 발급
- Refresh Token은 DB에 저장하여 관리(재로그인/탈취 방지)

#### ▶ 내 정보 조회 / 수정

- `GET /api/v1/users/me`
- `PATCH /api/v1/users/profile`

#### ▶ 비밀번호 변경 (`PATCH /api/v1/users/password`)

- 기존 비밀번호 검증 후 암호화 저장

#### ▶ 회원 탈퇴 (`DELETE /api/v1/users`)

- 유저 삭제 + Refresh Token DB에서도 삭제

---

### ✅ 3. JWT 토큰 관리 기능

#### ▶ Access Token

- 2시간 유효기간
- Authorization 헤더(`Bearer {token}`)로 전송

#### ▶ Refresh Token

- 7일 유효기간
- DB에 저장하여 만료 여부 및 사용자 일치 여부 검증
- 로테이션 방식으로 매번 새 Refresh Token 재발급

#### ▶ 토큰 재발급 API (`POST /api/v1/users/refresh`)

- Refresh Token → 검증 → 새 Access + Refresh Token 발급
- Refresh Token 탈취 위험 대비 로테이션 방식 적용
- 잘못된 토큰 / 만료된 토큰은 예외 처리

---

### ✅ 4. 책 리뷰(게시글) 기능

- 게시글 목록 조회 (페이징 + 정렬)
  - `GET /api/v1/posts?page=0&limit=10&sort=DATE`
- 게시글 상세 조회
  - `GET /api/v1/posts/{postId}`
- 게시글 작성 / 수정 / 삭제
  - `POST /api/v1/posts`
  - `PATCH /api/v1/posts/{postId}`
  - `DELETE /api/v1/posts/{postId}`
- 이미지(Base64) 저장 지원 (`image_url` 컬럼)

> **권한 제어**
>
> - 인증된 사용자만 작성/수정/삭제 가능
> - 작성자 본인이 아닌 경우 수정/삭제 불가

---

### ✅ 5. 댓글 & 좋아요 기능

#### 💬 댓글(Comment)

- 댓글 조회 / 작성 / 수정 / 삭제
- 본인 댓글만 수정·삭제 가능
- 엔티티 단위로 게시글과 연관 관계 유지

#### ❤️ 좋아요(LikeRecord)

- 좋아요 / 좋아요 취소
- (userId + postId) 복합키로 **중복 좋아요 방지**

---

### ✅ 6. 공통 인프라 & 유틸

- **ApiResponse<T>**

  - 모든 API를 `success / code / message / data` 구조로 통일

- **GlobalExceptionHandler**

  - JWT 인증 실패
  - Refresh Token 검증 실패
  - 유효성 검사 오류(ValidationException)
  - 404 / 400 / 401 / 403 등 공통 JSON 응답 처리

- **JPA Auditing (`BaseTimeEntity`)**

  - createdAt, updatedAt 자동 기록

- **Swagger / OpenAPI**

  - `/swagger-ui`에서 전체 API 문서 확인 가능

- **QueryDSL 기반 고급 검색**
  - 게시글·댓글 동적 조건 검색 구현

---

## 🛠 기술 스택

| 영역                 | 사용 기술                                         |
| -------------------- | ------------------------------------------------- |
| **Language**         | Java 17                                           |
| **Framework**        | Spring Boot 3.x                                   |
| **Web**              | Spring Web (REST API)                             |
| **Auth (인증/인가)** | Spring Security, **JWT (Access / Refresh Token)** |
| **Token**            | JJWT (io.jsonwebtoken) 기반 토큰 서명/검증        |
| **DB / ORM**         | MySQL, Spring Data JPA, Hibernate                 |
| **Query**            | QueryDSL (동적 쿼리용)                            |
| **Validation**       | Jakarta Bean Validation                           |
| **Documentation**    | springdoc-openapi (Swagger UI)                    |
| **Build Tool**       | Gradle (`./gradlew`)                              |
| **Testing**          | JUnit 5 (선택적 사용)                             |
| **Utilities**        | Lombok, Spring AOP, Actuator                      |
| **Infra**            | Refresh Token 저장용 JPA 엔티티 & Repository 운영 |

---

## 🔐 인증·인가 동작 흐름 (JWT 기반)

### **1) 회원가입**

**POST `/api/v1/users`**

- 요청 DTO: `SignupRequest`
- 비밀번호는 `BCryptPasswordEncoder` 로 암호화하여 저장
- 이메일 중복 여부 검사
- 기본 권한 `USER`
- 프로필 이미지(Base64) 저장 가능

---

### **2) 로그인 (JWT 발급)**

**POST `/api/v1/users/login`**

로그인 성공 시 서버는 **세션을 사용하지 않고** 다음을 발급합니다.

- **Access Token** (유효기간 2시간)
- **Refresh Token** (DB 저장, 유효기간 7일)

프로세스:

1. `AuthenticationManager` 가 이메일/비밀번호 인증 수행
2. 인증 성공 → `JwtTokenProvider` 가 Access / Refresh Token 생성
3. 기존 Refresh Token 삭제 후 새 token DB 저장
4. 로그인 응답으로 Access / Refresh Token 반환

> 더 이상 브라우저에 **JSESSIONID 쿠키가 발급되지 않음**  
> 서버는 완전히 **STATELESS** 로 동작합니다.

---

### **3) 인증이 필요한 API 호출 (Bearer Token 인증)**

모든 인증 요청은 다음 헤더를 포함합니다:
Authorization: Bearer {accessToken}

동작 과정:

1. `JwtAuthenticationFilter` 가 Authorization 헤더에서 JWT 추출
2. Access Token 유효성 검사
3. 토큰에서 email / userId 추출
4. `UserDetailsService` 로 사용자 조회
5. SecurityContext 에 인증 객체 즉시 세팅
6. 컨트롤러에서는 `@AuthenticationPrincipal UserPrincipal` 로 현재 사용자 접근

---

### **4) Access Token 만료 시 /refresh 로 재발급**

**POST `/api/v1/users/refresh`**

절차:

1. Refresh Token 유효성 검사
2. DB에 저장된 Refresh Token 존재 여부 확인
3. 만료 여부 검사
4. 새 Access Token + 새 Refresh Token 생성 (토큰 로테이션)
5. DB 저장 후 프론트에 반환

> Refresh Token 도난을 고려해 **DB 저장 + 토큰 로테이션 방식**으로 구현

---

### **5) 인가(Authorization) 처리**

**SecurityConfig.java**

- `/`, `/swagger-ui/**`, `/v3/api-docs/**`, `/actuator/health` → `permitAll()`
- 회원가입(`/users`), 로그인(`/users/login`), 토큰 재발급(`/users/refresh`) → `permitAll()`
- 게시글 조회(`/posts/**`) → `permitAll()`
- 그 외 `/api/v1/**` → **모두 인증 필요 (`authenticated()`)**

**비즈니스 로직에서 인가 처리**

- 게시글 수정/삭제
- 댓글 수정/삭제
- 회원 정보 수정/탈퇴 등

→ 모두 `principal.getId()` 와 리소스 소유자의 ID를 비교하여  
**본인만 접근할 수 있도록** 서비스 레벨에서 검증

---

## 🗄 주요 도메인 모델

### 👤 **User**

- 이메일 (unique)
- 암호화된 비밀번호
- 닉네임
- 프로필 이미지(Base64)
- 역할: `USER`, `ADMIN`
- `BaseTimeEntity` 로 생성/수정 시간 자동 기록

---

### 🔐 **RefreshToken**

> **JWT Refresh Token 저장용 엔티티**

- `userId`
- `token` (실제 Refresh Token 문자열)
- `expiresAt` (만료 시간)
- 로그인 시 기존 토큰 삭제 후 새 토큰 저장
- `/refresh` 요청에서 토큰 검증 및 재발급 진행

---

### 📖 **Post (리뷰 게시글)**

- 제목, 내용
- 이미지(Base64 URL)
- 작성자(User)
- 조회수, 좋아요 수, 댓글 수
- 관계
  - `@ManyToOne User` (작성자)
  - `@OneToMany Comment`
  - `@OneToMany LikeRecord`

---

### 💬 **Comment**

- 게시글에 대한 댓글
- 내용, 작성자
- 게시글 삭제 시 연관 댓글 함께 삭제
- 작성자만 수정/삭제 가능

---

### ❤️ **LikeRecord**

- 복합키 (사용자 + 게시글)
- 한 사용자가 한 게시글에 **좋아요 1회만 가능**
- 좋아요 / 좋아요 취소 기능 제공
