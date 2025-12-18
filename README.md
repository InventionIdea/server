# 발명아이디어 플랫폼

Spring Boot 애플리케이션으로, AI 기반 비디오 생성 기능을 통합한 플랫폼 백엔드입니다.

## 📋 목차

- [기술 스택](#기술-스택)
- [프로젝트 구조](#프로젝트-구조)
- [시작하기](#시작하기)
- [환경 설정](#환경-설정)
- [로컬 개발](#로컬-개발)
- [Docker 배포](#docker-배포)
- [API 문서](#api-문서)
- [비디오 생성 프로세스](#비디오-생성-프로세스)
- [ERD 다이어그램](#erd-다이어그램)

## 🛠 기술 스택

### Backend
- **Language**: Java 17
- **Framework**: Spring Boot 3.4.2
- **Build Tool**: Gradle 8.5
- **Database**: Supabase (PostgreSQL)
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security + JWT
- **Storage**: Cloudflare R2 (S3-compatible)
- **Documentation**: SpringDoc OpenAPI (Swagger)

### Video Generation
- **Language**: Python 3.11+
- **Libraries**:
  - `edge-tts`: 한국어 TTS 생성
  - `pollinations`: AI 이미지 생성
  - `pillow`: 이미지 처리
  - `googletrans`: 텍스트 번역
- **Tools**: FFmpeg (비디오 처리)

### Testing
- **In-memory DB**: H2
- **Test Framework**: JUnit 5, Spring Security Test

### DevOps
- **Containerization**: Docker (Multi-stage build)
- **Environment Management**: dotenv-java

## 📁 프로젝트 구조

```
.
├── src/                          # Spring Boot 소스 코드
│   ├── main/
│   │   ├── java/
│   │   │   └── iakka/platform/
│   │   │       ├── config/       # 설정 클래스
│   │   │       ├── domain/       # 도메인별 모듈
│   │   │       │   ├── auth/     # 인증
│   │   │       │   ├── idea/     # 아이디어 (비디오 생성)
│   │   │       │   ├── post/     # 게시글
│   │   │       │   ├── comment/  # 댓글
│   │   │       │   ├── like/     # 좋아요
│   │   │       │   ├── crew/     # 크루
│   │   │       │   ├── user/    # 사용자
│   │   │       │   ├── chat/    # 채팅
│   │   │       │   └── trend/   # 트렌드
│   │   │       ├── global/       # 전역 서비스 (R2StorageService)
│   │   │       └── jwt/          # JWT 관련
│   │   └── resources/
│   │       └── application.yml   # 애플리케이션 설정
│   └── test/                     # 테스트 코드
├── scripts/                      # Python 비디오 생성 스크립트
│   ├── generate_video.py         # 메인 진입점 (CLI)
│   ├── services/                 # 비디오 생성 서비스 모듈
│   │   ├── tts.py               # TTS 생성
│   │   ├── image.py             # 이미지 생성
│   │   ├── video.py             # 비디오 처리
│   │   └── bgm.py               # BGM 추가
│   ├── resource/                 # 리소스 파일
│   │   ├── anemone.ttf          # 한글 폰트
│   │   ├── background.png       # 배경 이미지
│   │   └── bgm.mp3              # 배경 음악
│   └── requirements.txt          # Python 의존성
├── img/                          # 문서용 이미지
│   └── erd.png                   # ERD 다이어그램
├── build.gradle                  # Gradle 빌드 설정
├── settings.gradle               # Gradle 프로젝트 설정
├── Dockerfile                    # Docker 이미지 빌드
├── .dockerignore                 # Docker 빌드 제외 파일
├── .env.example                  # 환경 변수 예시
└── README.md                     # 이 파일
```

## 🚀 시작하기

### 필수 요구사항

- **Java**: 17 이상
- **Python**: 3.11 이상
- **FFmpeg**: 비디오 처리용 (PATH에 추가 필요)
- **Supabase**: 데이터베이스 계정
- **Cloudflare R2**: 스토리지 계정

### 환경 설정

1. **저장소 클론**
   ```bash
   git clone <repository-url>
   cd iakka
   ```

2. **환경 변수 설정**
   ```bash
   cp .env.example .env
   ```
   
   `.env` 파일을 열어 실제 값으로 수정:
   ```env
   # Supabase Database
   SUPABASE_URL=jdbc:postgresql://your-project-ref.supabase.co:6543/postgres?sslmode=require
   SUPABASE_USER=postgres.your-project-ref
   SUPABASE_PASSWORD=your-password
   
   # JWT
   JWT_SECRET=your-secure-secret-key
   
   # Cloudflare R2
   R2_ENDPOINT=https://your-account-id.r2.cloudflarestorage.com
   R2_ACCESS_KEY=your-access-key
   R2_SECRET_KEY=your-secret-key
   R2_BUCKET=your-bucket-name
   ```

3. **Python 의존성 설치**
   ```bash
   cd scripts
   pip install -r requirements.txt
   cd ..
   ```

4. **FFmpeg 설치**
   - **Windows**: [FFmpeg 다운로드](https://ffmpeg.org/download.html) 후 PATH 추가
   - **macOS**: `brew install ffmpeg`
   - **Linux**: `sudo apt-get install ffmpeg`

## 💻 로컬 개발

### 애플리케이션 실행

#### Windows (PowerShell)
```powershell
# 테스트 실행
.\gradlew.bat test

# 애플리케이션 실행
.\gradlew.bat bootRun
```

#### Linux / macOS
```bash
# 테스트 실행
./gradlew test

# 애플리케이션 실행
./gradlew bootRun
```

애플리케이션이 실행되면 `http://localhost:8080`에서 접근 가능합니다.

### Swagger UI

API 문서는 다음 URL에서 확인할 수 있습니다:
```
http://localhost:8080/swagger-ui.html
```

## 🐳 Docker 배포

### 이미지 빌드

```bash
docker build -t iakka-platform .
```

### 컨테이너 실행

```bash
docker run -d \
  -p 8080:8080 \
  --env-file .env \
  --name iakka-platform \
  iakka-platform
```

또는 환경 변수를 직접 지정:

```bash
docker run -d \
  -p 8080:8080 \
  -e SUPABASE_URL=... \
  -e SUPABASE_USER=... \
  -e SUPABASE_PASSWORD=... \
  -e JWT_SECRET=... \
  -e R2_ENDPOINT=... \
  -e R2_ACCESS_KEY=... \
  -e R2_SECRET_KEY=... \
  -e R2_BUCKET=... \
  --name iakka-platform \
  iakka-platform
```

## 📚 API 문서

### 인증 (Auth)

| Method | Endpoint | 설명 | Body |
|--------|----------|------|------|
| POST | `/auth/register` | 회원가입 | RegisterRequest |
| POST | `/auth/login` | 로그인 | LoginRequest |

### 아이디어 (Idea) - 비디오 생성

| Method | Endpoint | 설명 | Body |
|--------|----------|------|------|
| POST | `/ideas/generate` | 아이디어 영상 생성 | IdeaRequest |
| GET | `/ideas/list/{userId}` | 사용자 아이디어 조회 | - |
| DELETE | `/ideas/{ideaId}` | 아이디어 삭제 | - |

### 게시글 (Post)

| Method | Endpoint | 설명 | Body |
|--------|----------|------|------|
| GET | `/posts` | 게시글 전체 조회 | - |
| POST | `/posts` | 게시글 생성 | PostRequest |
| PUT | `/posts/{id}` | 게시글 수정 | PostRequest |
| DELETE | `/posts/{id}` | 게시글 삭제 | - |
| GET | `/posts/user/{userId}` | 특정 유저의 게시글 | - |
| GET | `/posts/search` | 게시글 검색 | - |

### 댓글 (Comment)

| Method | Endpoint | 설명 | Body |
|--------|----------|------|------|
| POST | `/comments` | 댓글 생성 | CommentRequest |
| PUT | `/comments/{commentId}` | 댓글 수정 | CommentRequest |
| DELETE | `/comments/{commentId}` | 댓글 삭제 | - |
| GET | `/comments/{type}/{targetId}` | 댓글 조회 | - |

### 좋아요 (Like)

| Method | Endpoint | 설명 | Body |
|--------|----------|------|------|
| POST | `/likes/{type}/{targetId}` | 좋아요 등록 | User |
| DELETE | `/likes/{type}/{targetId}` | 좋아요 취소 | User |
| GET | `/likes/{type}/{targetId}/liked` | 좋아요 여부 확인 | - |
| GET | `/likes/{type}/{targetId}/count` | 좋아요 수 조회 | - |

### 크루 (Crew)

| Method | Endpoint | 설명 | Body |
|--------|----------|------|------|
| POST | `/crews` | 크루 생성 | CrewRequest |
| PUT | `/crews/{crewId}` | 크루 정보 수정 | CrewRequest |
| POST | `/crews/{crewId}/join/{userId}` | 크루 가입 | - |
| POST | `/crews/{crewId}/leave/{userId}` | 크루 탈퇴 | - |

### 사용자 (User)

| Method | Endpoint | 설명 | Body |
|--------|----------|------|------|
| GET | `/users/{userId}/points` | 포인트 조회 | - |
| POST | `/users/{userId}/points/add` | 포인트 추가 | - |
| POST | `/users/{userId}/points/deduct` | 포인트 차감 | - |
| DELETE | `/users/{userId}` | 사용자 삭제 | - |

### 트렌드 (Trend)

| Method | Endpoint | 설명 | Body |
|--------|----------|------|------|
| GET | `/trend` | 트렌드 조회 | - |

## 🎬 비디오 생성 프로세스

비디오 생성은 `/ideas/generate` 엔드포인트를 통해 수행됩니다.

### 프로세스 흐름

1. **요청 수신**: 클라이언트가 `IdeaRequest` (userId, title, script)를 전송
2. **Python 스크립트 실행**: Java 애플리케이션이 `scripts/generate_video.py` 실행
3. **비디오 생성** (Python):
   - 각 문장별로 TTS 생성 (edge-tts)
   - AI 이미지 생성 (pollinations)
   - 이미지 오버레이 및 텍스트 추가
   - 문장별 비디오 생성 (FFmpeg)
   - 모든 비디오 병합
   - BGM 추가
4. **파일 경로 반환**: Python 스크립트가 생성된 비디오 파일의 절대 경로를 stdout으로 출력
5. **R2 업로드**: Java 애플리케이션이 `R2StorageService`를 통해 Cloudflare R2에 업로드
6. **데이터베이스 저장**: R2 URL을 `Idea` 엔티티의 `fileId`에 저장
7. **로컬 파일 삭제**: 업로드 완료 후 로컬 임시 파일 삭제

### 요청 예시

```json
POST /ideas/generate
Content-Type: application/json

{
  "userId": "user123",
  "title": "나의 첫 번째 아이디어",
  "script": [
    "안녕하세요. 오늘은 새로운 아이디어를 소개합니다.",
    "이 아이디어는 혁신적인 기술을 활용합니다.",
    "많은 사람들에게 도움이 될 것입니다."
  ]
}
```

### 응답 예시

```json
{
  "id": 1,
  "userId": "user123",
  "title": "나의 첫 번째 아이디어",
  "fileId": "https://your-account-id.r2.dev/videos/user123/user123_1234567890.mp4",
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00",
  "views": 0
}
```

## 🗄 ERD 다이어그램

![ERD](img/erd.png)

주요 엔티티:
- **User**: 사용자 정보
- **Idea**: 아이디어 (비디오 메타데이터)
- **Post**: 게시글
- **Comment**: 댓글
- **Like**: 좋아요
- **Crew**: 크루
- **CrewMember**: 크루 멤버
- **TrendSnapshot**: 트렌드 스냅샷

## 🔧 개발 가이드

### 코드 스타일

- Java: Google Java Style Guide 기반
- Python: PEP 8 준수
- 커밋 메시지: Conventional Commits 형식 권장

### 테스트 실행

```bash
# 전체 테스트
./gradlew test

# 특정 테스트 클래스만 실행
./gradlew test --tests IdeaServiceTest
```

### 빌드

```bash
# JAR 파일 빌드
./gradlew clean build

# 빌드된 JAR 실행
java -jar build/libs/platform-0.0.1-SNAPSHOT.jar
```

## 📝 주의사항

- `.env` 파일은 절대 버전 관리에 포함하지 마세요
- 실제 운영 환경에서는 `JWT_SECRET`을 충분히 긴 랜덤 문자열로 설정하세요
- Supabase는 Transaction Pooler (포트 6543) 사용을 권장합니다
- Python 스크립트는 FFmpeg가 PATH에 있어야 정상 작동합니다
- 비디오 생성은 시간이 걸릴 수 있으므로 타임아웃 설정을 고려하세요

## 📄 License

This project is licensed under the MIT License. See `LICENSE` for details.
