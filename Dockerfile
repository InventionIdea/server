# 1. Python 3.12 기반 이미지 사용
FROM python:3.12

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 기본 패키지 업데이트 및 FFmpeg, ImageMagick 설치
RUN apt-get update && apt-get install -y \
    ffmpeg \
    imagemagick \
    && rm -rf /var/lib/apt/lists/*

# 4. ImageMagick 정책 수정 (보안 제한 해제 - 필요시 조정)
RUN echo "<?xml version='1.0' encoding='UTF-8'?>" > /etc/ImageMagick-6/policy.xml && \
    echo "<policymap>" >> /etc/ImageMagick-6/policy.xml && \
    echo "  <policy domain='coder' rights='read | write' pattern='*' />" >> /etc/ImageMagick-6/policy.xml && \
    echo "  <policy domain='path' rights='read | write' pattern='*' />" >> /etc/ImageMagick-6/policy.xml && \
    echo "</policymap>" >> /etc/ImageMagick-6/policy.xml

# 5. requirements.txt 복사 및 패키지 설치
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 6. 프로젝트 소스 코드 복사
COPY . .

# 7. 포트 노출 (FastAPI 기본 포트)
EXPOSE 8000

# 8. FastAPI 실행
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]