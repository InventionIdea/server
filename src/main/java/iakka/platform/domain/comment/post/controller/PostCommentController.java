package iakka.platform.domain.comment.post.controller;

import iakka.platform.domain.comment.post.dto.PostCommentRequest;
import iakka.platform.domain.comment.post.service.PostCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post-comments")
public class PostCommentController {
    private final PostCommentService postCommentService;

    public PostCommentController(PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody PostCommentRequest request) {
        return postCommentService.createComment(request);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getComments(@PathVariable Long postId) {
        return postCommentService.getComments(postId);
    }
}