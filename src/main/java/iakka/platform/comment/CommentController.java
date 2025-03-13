package iakka.platform.comment;

import iakka.platform.post.Post;
import iakka.platform.post.PostRepository;
import iakka.platform.user.User;
import iakka.platform.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentController(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    /**
     * 특정 게시글의 최상위 댓글(부모가 없는 댓글) 조회
     */
    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@RequestParam Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return ResponseEntity.ok(commentRepository.findByPostIdAndParentCommentIsNull(postId));
    }

    /**
     * 특정 댓글의 대댓글 조회
     */
    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<Comment>> getReplies(@PathVariable Long commentId) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));

        return ResponseEntity.ok(commentRepository.findByParentCommentId(commentId));
    }

    /**
     * 댓글 또는 대댓글 생성
     */
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentRequest commentRequest) {
        if (commentRequest.getPostId() == null || commentRequest.getAuthorId() == null) {
            return ResponseEntity.badRequest().body("Error: postId and authorId are required");
        }

        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User author = userRepository.findById(commentRequest.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment parentComment = null;
        if (commentRequest.getParentCommentId() != null) {
            parentComment = commentRepository.findById(commentRequest.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        }

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setParentComment(parentComment); // 부모 댓글 설정 (없으면 null)

        return ResponseEntity.ok(commentRepository.save(comment));
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id, @RequestBody CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getAuthor().getId().equals(commentRequest.getAuthorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: Only the author can update this comment");
        }

        comment.setContent(commentRequest.getContent());
        return ResponseEntity.ok(commentRepository.save(comment));
    }

    /**
     * 댓글 또는 대댓글 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, @RequestParam Long authorId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getAuthor().getId().equals(authorId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: Only the author can delete this comment");
        }

        // 먼저 해당 댓글의 대댓글이 있으면 모두 삭제
        commentRepository.deleteAllByParentCommentId(id);

        // 댓글 삭제
        commentRepository.delete(comment);

        return ResponseEntity.noContent().build();
    }
}

/**
 * 댓글 요청을 위한 DTO
 */
class CommentRequest {
    private Long postId;
    private Long authorId;
    private Long parentCommentId; // 부모 댓글 ID 추가 (대댓글인 경우)
    private String content;

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public Long getParentCommentId() { return parentCommentId; }
    public void setParentCommentId(Long parentCommentId) { this.parentCommentId = parentCommentId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}