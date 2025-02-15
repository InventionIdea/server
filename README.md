# 📆 최종 업데이트: 2025-02-16
## ✅ 다음 업데이트 예정
- 댓글 수정 및 삭제 기능
- 게시글 수정 기능
- JWT 인증 적용
---
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