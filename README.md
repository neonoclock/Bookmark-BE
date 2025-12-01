# 📚 Bookmark — 책 리뷰 공유 서비스 (Backend)

**Bookmark** 는 읽은 책을 기록하고, 다른 독자들과 리뷰를 나누는 웹 서비스입니다.  
이 레포지토리는 **백엔드 전체**를 Spring Boot 기반으로 구현한 프로젝트로,  
Vanilla JS 기반의 Frontend 애플리케이션과 REST API로 연동됩니다.

---

## ✨ 주요 기능 (Backend)

### ✅ 1. 사용자 인증 · 인가 (Spring Security)

- Spring Security 기반 **세션 로그인 구조**
- `BCryptPasswordEncoder` 를 사용한 비밀번호 암호화
- `CustomUserDetailsService` + `UserPrincipal` 로 사용자 인증 정보 관리
- `/api/v1/users/login` 에서 이메일·비밀번호로 로그인 처리
- `/api/v1/users/me` 를 통해 현재 로그인한 사용자 정보 조회
- 프로필 수정 / 비밀번호 변경 / 회원 탈퇴 등은
  `@AuthenticationPrincipal UserPrincipal` 기반으로 **로그인한 본인만 접근 가능**

### ✅ 2. 회원 관리(User)

- 회원가입 (`POST /api/v1/users`)
  - 이메일 중복 체크
  - 비밀번호 암호화 저장
  - 기본 권한 `USER` 부여
  - 프로필 이미지(Base64 문자열) 저장 가능
- 로그인 (`POST /api/v1/users/login`)
  - Spring Security `AuthenticationManager` 를 이용한 인증
  - 성공 시 서버 세션에 Authentication 저장
- 내 정보 조회 / 수정 (`GET /api/v1/users/me`, `PATCH /api/v1/users/profile`)
- 비밀번호 변경 (`PATCH /api/v1/users/password`)
  - 기존 비밀번호 검증 → 새 비밀번호 암호화 후 저장
- 회원 탈퇴 (`DELETE /api/v1/users`)
  - 로그인한 사용자 기준으로 삭제

### ✅ 3. 책 리뷰(게시글) 기능

- 게시글 목록 조회 (페이징 + 정렬)
  - `GET /api/v1/posts?page=0&limit=10&sort=DATE`
- 게시글 상세 조회
  - `GET /api/v1/posts/{postId}`
- 게시글 작성 / 수정 / 삭제
  - `POST /api/v1/posts`
  - `PATCH /api/v1/posts/{postId}`
  - `DELETE /api/v1/posts/{postId}`
- 게시글 이미지를 Base64 문자열로 저장 (`image_url` 컬럼)

> **권한 제어**  
> - 로그인한 사용자만 작성/수정/삭제 가능  
> - 작성자 본인이 아닌 경우 수정/삭제 불가

### ✅ 4. 댓글 & 좋아요 기능

- 댓글 목록 조회
  - `GET /api/v1/posts/{postId}/comments`
- 댓글 작성 / 수정 / 삭제
  - `POST /api/v1/posts/{postId}/comments`
  - `PATCH /api/v1/posts/{postId}/comments/{commentId}`
  - `DELETE /api/v1/posts/{postId}/comments/{commentId}`
- 게시글 좋아요 / 좋아요 취소
  - `POST /api/v1/posts/{postId}/like`
  - `DELETE /api/v1/posts/{postId}/like`

> 댓글과 좋아요 역시 **로그인 사용자만** 사용할 수 있고,  
> 댓글 수정·삭제는 **작성자 본인만** 가능하도록 서비스 레벨에서 검증합니다.

### ✅ 5. 공통 인프라 & 유틸

- `ApiResponse<T>`  
  - 모든 API 응답을 `success / code / message / data` 구조로 통일
- `PagedResponse<T>`  
  - 페이징 응답을 위한 공통 DTO
- 공통 예외 처리 (`GlobalExceptionHandler`)
  - `NotFoundException`, `UnauthorizedException`, `InvalidRequestException` 등
  - 예외를 일관된 JSON 응답 형식으로 변환
- JPA Auditing (`BaseTimeEntity`)
  - `createdAt`, `updatedAt` 자동 관리
- Swagger / OpenAPI
  - `springdoc-openapi` 를 사용해 `/swagger-ui` 로 API 문서 제공
- QueryDSL 설정 (`QuerydslConfig`)
  - 복잡한 게시글/댓글 조회용 QueryDSL 기반 Repository 구현

---

## 🛠 기술 스택

| 영역 | 사용 기술 |
|------|-----------|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.x |
| **Web** | Spring Web (spring-boot-starter-web) |
| **DB / ORM** | MySQL, Spring Data JPA, Hibernate |
| **Query** | QueryDSL (JPA, annotation processor 기반) |
| **Validation** | Jakarta Bean Validation (spring-boot-starter-validation) |
| **Auth** | Spring Security, BCryptPasswordEncoder |
| **API 문서** | springdoc-openapi (Swagger UI) |
| **빌드 도구** | Gradle (Gradle Wrapper `./gradlew`) |
| **기타** | Spring AOP, Actuator, Lombok |

---
## 🔐 인증·인가 동작 흐름

### **1) 회원가입**
**POST `/api/v1/users`**  
- 요청 DTO: `SignupRequest`  
- 비밀번호를 `BCryptPasswordEncoder` 로 암호화 후 DB 저장  
- 이메일 중복 검사 후 User 저장  

---

### **2) 로그인**
**POST `/api/v1/users/login`**

- `AuthenticationManager` 가  
  `CustomUserDetailsService + DaoAuthenticationProvider` 를 사용해 인증  
- 인증 성공 시  
  → 서버 세션에 `Authentication` 저장  
  → 브라우저에는 **JSESSIONID 쿠키 발급**

---

### **3) 인증이 필요한 API 호출**

- 프론트에서 `credentials: "include"` 옵션을 사용해 fetch 요청
- Spring Security가 **JSESSIONID 쿠키**를 보고 인증 여부 판단
- 컨트롤러에서는  
  `@AuthenticationPrincipal UserPrincipal principal` 로 로그인 사용자 정보 접근

---

### **4) 인가(권한) 처리**

**SecurityConfig.java**

- 회원가입·로그인·Swagger 등  
  → `permitAll()`
- 그 외 `/api/v1/**` 요청  
  → `authenticated()` 로 보호

**비즈니스 로직에서**

- 리소스 소유자인지 확인  
- `authorId == principal.getId()` 조건으로  
  → 본인만 수정/삭제/탈퇴 가능

---

## 🗄 주요 도메인 모델

### 👤 **User**
- 이메일  
- 암호화된 비밀번호  
- 닉네임  
- 프로필 이미지(Base64)  
- 사용자 역할(UserRole: `USER`, `ADMIN`)  
- `BaseTimeEntity` 상속으로 생성·수정 시간 자동 기록

---

### 📖 **Post (리뷰 게시글)**

- 제목, 내용, 이미지(Base64 URL)  
- 작성자(User)  
- 조회수, 좋아요 수, 댓글 수  
- 관계
  - `@ManyToOne User` — 작성자  
  - `@OneToMany Comment`  
  - `@OneToMany LikeRecord`  

---

### 💬 **Comment**
- 특정 게시글의 댓글  
- 내용, 작성자, 작성시간 포함  
- 게시글 삭제 시 연관 댓글 함께 정리

---

### ❤️ **LikeRecord**
- 복합키(사용자 + 게시글) 기반 좋아요 기록  
- 한 사용자가 한 게시글에 **한 번만 좋아요** 가능하도록 제약

---

## 🚀 로컬 실행 방법

### **1) 레포지토리 클론**

```bash
git clone https://github.com/neonoclock/Bookmark-BE.git
cd Bookmark-BE/BE
