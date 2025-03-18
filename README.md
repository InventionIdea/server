## ✅ 다음 업데이트 예정
- 크투 톡방 기능
- 트렌드? 기능 추가
- spring security + jwt
- idea 생성 관련 문제 해결

## 🚀 업데이트 내역 (2025-03-17)

- 크루 기능 추가
- 크루 톡방 기능은 에러 있음

## 🚀 업데이트 내역 (2025-03-17)

- refactor: security config 파일 위치 조정
- fast api와 통신 문제 해결
  - 그러나 같은 user가 같은 대본으로 요청을 한다면 기존 파일도 file id가 변경된다는 문제가 있었음, 이에 대한 문제를 해결하기 위해 ID를 기준으로 업데이트 하는 방식으로 변경해줘야 할 것 같음.

## 🚀 업데이트 내역 (2025-03-16)

- 포인트 시스템 구현
- refactor: auth controller -> user controller

## 🚀 업데이트 내역 (2025-03-15)

- 좋아요 기능 구현
- 사용자별 게시글 목록 조회
- 게시글 검색기능 구현

## 🚀 업데이트 내역 (2025-03-13)

### 🔹 댓글(Comment) 기능
- 대댓글 작성 (`POST /comments`)
- 특정 댓글의 대댓글 조회 (`GET /comments/{commentId}/replies`)
- 댓글 삭제 시 대댓글 함께 삭제 (`DELETE /comments/{id}?authorId={authorId}`)

### 🔹 API 개선
- 댓글 조회 시 부모 댓글과 대댓글 분리 제공
- API 응답 형식 개선 및 오류 메시지 명확화

## 🚀 업데이트 내역 (2025-03-13)

### 🔹 게시글(Post) 기능
- 게시글 수정 (`PUT /posts/{id}`)
- 게시글 삭제 (`DELETE /posts/{id}?authorId={authorId}`)

### 🔹 댓글(Comment) 기능
- 댓글 수정 (`PUT /comments/{id}`)
- 댓글 삭제 (`DELETE /comments/{id}?authorId={authorId}`)

## 🚀 업데이트 내역 (2025-02-16)

### 🔹 사용자(User) 기능
- 사용자 회원가입 (`POST /auth/register`)

### 🔹 게시글(Post) 기능
- 게시글 작성 (`POST /posts`)
- 게시글 조회 (`GET /posts`)
- 게시글 좋아요 (`POST /posts/{id}/like`)

### 🔹 댓글(Comment) 기능
- 댓글 작성 (`POST /comments`)
- 특정 게시글의 댓글 조회 (`GET /comments?postId={id}`)