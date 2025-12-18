# InventionIdea Platform

Monolithic Spring Boot application for the platform backend with integrated video generation.

## 기술 스택

### 🧱 Backend

- **Language**: Java 17
- **Framework**: Spring Boot 3.4.2
- **Build Tool**: Gradle
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA
- **Security**: Spring Security + JWT
- **Storage**: Cloudflare R2 (S3-compatible)

### 🎬 Video Generation

- **Language**: Python 3.10+
- **Libraries**: edge-tts, pollinations, pillow, googletrans

### 🧪 Test

- **In-memory DB**: H2
- **Test Frameworks**: JUnit 5, Spring Security Test

### 🔧 기타

- **Lombok**: 반복 코드 제거
- **AWS SDK**: S3-compatible storage (R2)

## 프로젝트 구조

```
.
├── src/                    # Spring Boot 소스 코드
│   ├── main/
│   │   ├── java/          # Java 소스
│   │   └── resources/    # 설정 파일
│   └── test/              # 테스트 코드
├── scripts/                # Python 비디오 생성 스크립트
│   ├── generate_video.py  # 메인 진입점
│   ├── services/          # 비디오 생성 서비스 모듈
│   └── resource/          # 리소스 파일 (폰트, 배경, BGM)
├── build.gradle           # Gradle 빌드 설정
└── README.md              # 이 파일
```

## Quick Start

### 환경 설정

1. 환경 변수 설정 (`.env` 파일 또는 시스템 환경 변수):
   ```
   DB_URL=jdbc:postgresql://localhost:5432/your_db
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   JWT_SECRET=your_jwt_secret
   R2_ENDPOINT=https://your-account-id.r2.cloudflarestorage.com
   R2_ACCESS_KEY=your_access_key
   R2_SECRET_KEY=your_secret_key
   R2_BUCKET=your_bucket_name
   R2_PUBLIC_URL_BASE=https://your-domain.com  # 선택사항
   ```

2. Python 의존성 설치:
   ```powershell
   cd scripts
   pip install -r requirements.txt
   ```

3. FFmpeg 설치 (비디오 처리용):
   - Windows: [FFmpeg 다운로드](https://ffmpeg.org/download.html)
   - PATH에 FFmpeg 추가 필요

### 실행

#### Windows PowerShell
```powershell
# 테스트 실행
.\gradlew.bat test

# 애플리케이션 실행
.\gradlew.bat bootRun
```

#### Linux/Mac
```bash
# 테스트 실행
./gradlew test

# 애플리케이션 실행
./gradlew bootRun
```

## ERD 다이어그램

![ERD](img/erd.png)

## API 문서

Swagger UI: `http://localhost:8080/swagger-ui.html`

### 📝 Post 관련 (post-controller)

| Method | Endpoint                          | 설명              | 요청 파라미터                        | Body           |
|--------|-----------------------------------|-------------------|--------------------------------------|----------------|
| GET    | `/posts`                          | 게시글 전체 조회   | 없음                                 | ❌              |
| POST   | `/posts`                          | 게시글 생성        | 없음                                 | ✅ PostRequest  |
| PUT    | `/posts/{id}`                     | 게시글 수정        | `id` (path)                          | ✅ PostRequest  |
| DELETE | `/posts/{id}`                     | 게시글 삭제        | `id` (path), `authorId` (query)      | ❌              |
| GET    | `/posts/user/{userId}`            | 특정 유저의 게시글 | `userId` (path)                      | ❌              |
| GET    | `/posts/search`                   | 게시글 검색        | `keyword` (query)                    | ❌              |

### 💬 Comment 관련 (comment-controller)

| Method | Endpoint                              | 설명            | 요청 파라미터                                 | Body             |
|--------|---------------------------------------|------------------|-----------------------------------------------|------------------|
| POST   | `/comments`                           | 댓글 생성         | 없음                                          | ✅ CommentRequest |
| PUT    | `/comments/{commentId}`               | 댓글 수정         | `commentId` (path)                            | ✅ CommentRequest |
| DELETE | `/comments/{commentId}`               | 댓글 삭제         | `commentId` (path)                            | ❌                |
| GET    | `/comments/{type}/{targetId}`         | 댓글 조회         | `type`, `targetId` (path)                     | ❌                |

### ❤️ Like 관련 (like-controller)

| Method | Endpoint                                 | 설명              | 요청 파라미터                                      | Body   |
|--------|------------------------------------------|-------------------|----------------------------------------------------|--------|
| POST   | `/likes/{type}/{targetId}`               | 좋아요 등록        | `type`, `targetId` (path)                          | ✅ User |
| DELETE | `/likes/{type}/{targetId}`               | 좋아요 취소        | `type`, `targetId` (path)                          | ✅ User |
| GET    | `/likes/{type}/{targetId}/liked`         | 좋아요 여부 확인    | `type`, `targetId` (path), `user` (query)          | ❌      |
| GET    | `/likes/{type}/{targetId}/count`         | 좋아요 수 조회      | `type`, `targetId` (path)                          | ❌      |

### 👥 Crew 관련 (crew-controller)

| Method | Endpoint                                 | 설명               | 요청 파라미터                               | Body            |
|--------|------------------------------------------|--------------------|---------------------------------------------|-----------------|
| POST   | `/crews`                                 | 크루 생성           | 없음                                        | ✅ CrewRequest   |
| PUT    | `/crews/{crewId}`                        | 크루 정보 수정       | `crewId` (path)                             | ✅ CrewRequest   |
| POST   | `/crews/{crewId}/join/{userId}`          | 크루 가입            | `crewId`, `userId` (path)                   | ❌               |
| POST   | `/crews/{crewId}/leave/{userId}`         | 크루 탈퇴            | `crewId`, `userId` (path)                   | ❌               |

### 💡 Idea 관련 (idea-controller)

| Method | Endpoint                          | 설명                   | 요청 파라미터              | Body               |
|--------|-----------------------------------|------------------------|-----------------------------|--------------------|
| POST   | `/ideas/generate`                 | 아이디어 영상 생성      | 없음                        | ✅ IdeaRequest      |
| POST   | `/ideas/update-file-id`           | 아이디어 파일 ID 수정   | 없음                        | ✅ FileUpdateRequest|
| GET    | `/ideas/list/{userId}`            | 사용자 아이디어 조회    | `userId` (path)             | ❌                  |
| DELETE | `/ideas/{ideaId}`                 | 아이디어 삭제           | `ideaId` (path)             | ❌                  |

### 👤 User 관련 (user-controller)

| Method | Endpoint                              | 설명               | 요청 파라미터                           | Body  |
|--------|---------------------------------------|--------------------|-----------------------------------------|--------|
| GET    | `/users/{userId}/points`              | 포인트 조회         | `userId` (path)                         | ❌      |
| POST   | `/users/{userId}/points/add`          | 포인트 추가         | `userId` (path), `amount` (query)       | ❌      |
| POST   | `/users/{userId}/points/deduct`       | 포인트 차감         | `userId` (path), `amount` (query)       | ❌      |
| DELETE | `/users/{userId}`                     | 사용자 삭제         | `userId` (path)                         | ❌      |

### 🔐 Auth 관련 (auth-controller)

| Method | Endpoint           | 설명       | 요청 파라미터 | Body             |
|--------|--------------------|------------|----------------|------------------|
| POST   | `/auth/register`   | 회원가입    | 없음           | ✅ RegisterRequest|
| POST   | `/auth/login`      | 로그인      | 없음           | ✅ LoginRequest   |

### 📊 Trend 관련 (trend-controller)

| Method | Endpoint       | 설명           | 요청 파라미터 | Body |  
|--------|----------------|----------------|----------------|------|
| GET    | `/trend`       | 트렌드 조회     | 없음           | ❌    |

## 비디오 생성 프로세스

1. 사용자가 `/ideas/generate` 엔드포인트에 요청
2. Java 애플리케이션이 Python 스크립트 실행 (`scripts/generate_video.py`)
3. Python 스크립트가 다음 작업 수행:
   - TTS 생성 (edge-tts)
   - 이미지 생성 (pollinations)
   - 이미지 오버레이 및 텍스트 추가
   - 비디오 생성 및 병합
   - BGM 추가
4. 생성된 비디오 파일을 Cloudflare R2에 업로드
5. R2 URL을 데이터베이스에 저장

## Requirements

- Java 17+
- Python 3.10+
- FFmpeg (비디오 처리용)
- PostgreSQL
- Cloudflare R2 계정 (또는 S3-compatible storage)

## Notes

- 로컬 시크릿 파일 (`.env`, API 토큰 등)은 워크스페이스에 두고 추적하지 않도록 설정
- 루트 `.gitignore`는 두 프로젝트의 공통 빌드 아티팩트를 제외하도록 구성됨

## License

This project is licensed under the MIT License. See `LICENSE` for details.
