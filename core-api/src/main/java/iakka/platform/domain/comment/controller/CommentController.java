package iakka.platform.domain.comment.controller;

import iakka.platform.domain.comment.dto.CommentRequest;
import iakka.platform.domain.comment.entity.Comment.CommentType;
import iakka.platform.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentRequest request) {
        return commentService.createComment(request);
    }

    @GetMapping("/{type}/{targetId}")
    public ResponseEntity<?> getComments(@PathVariable CommentType type,
                                         @PathVariable Long targetId) {
        return commentService.getComments(type, targetId);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId,
                                           @RequestBody CommentRequest request) {
        return commentService.updateComment(commentId, request);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        return commentService.deleteComment(commentId);
    }
}
