# 🎮 AI Video Generator API

사용자의 아이디어를 바탕으로 영상 콘텐츠를 생성하는 FastAPI 기반 API 서버입니다.  
Text-to-Image, Text-to-Sound AI를 사용하여 영상 파일을 생성하고, 이를 구글 드라이브에 저장한 후, 파일ID를 Spirng 서버로 반환합니다.

## ⚙️ 기술 스택

- **Python** 3.12
- **FastAPI**
- **FFmpeg** / **ImageMagick** (영상/이미지 처리)
- **Docker** (배포 및 실행 환경)
- **Uvicorn** (ASGI 서버)


## 🚀 실행 방법

### Docker로 실행

```bash
# 1. 이미지 빌드
docker build -t ai-video-api .

# 2. 캔테이너 실행
docker run -d -p 8000:8000 --name video-api ai-video-api
```

### 로컬에서 직접 실행 (선택)

```bash
pip install -r requirements.txt
uvicorn main:app --reload
```

## 📘 주요 API

| Method | Endpoint                | 설명                   |
|--------|-------------------------|------------------------|
| POST   | `/ideas/generate`       | 텍스트 기반 영상 생성    |
| POST   | `/ideas/update-file-id` | 생성된 영상 파일 ID 수정 |

> 문서 페이지: [http://localhost:8000/docs](http://localhost:8000/docs)


## 📂 디렉터리 구조

```bash
video_api-main/
├── .gitignore
├── Dockerfile
├── README.md
├── main.py                     # FastAPI 진입점
├── output/                     # 생성된 영상 저장 폴더(임시, 영상 업로드 직후에 디렉토리 삭제)
│   └── .gitkeep
├── requirements.txt            # 패키지 목록
├── resource/                   # 영상 제작에 사용될 리소스
│   ├── anemone.ttf             # 텍스트 폰트
│   ├── background.png          # 기본 배경 이미지
│   └── bgm.mp3                 # 기본 배경 음악 (추후 변경 예정)
└── services/                   # 주요 서비스 로직
    ├── bgm.py                  # BGM 처리
    ├── image.py                # 이미지 생성/처리 (pollinations.ai)
    ├── tts.py                  # 텍스트 음성 변환 (edge TTS -> 추후 유료 서비스로 전환 예정)
    ├── upload.py               # 파일 저장 및 업로드 (Google Drive)
    └── video.py                # 영상 생성 로직
```