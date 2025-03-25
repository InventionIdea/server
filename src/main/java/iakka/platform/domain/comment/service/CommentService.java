package iakka.platform.domain.comment.service;

import iakka.platform.domain.comment.dto.CommentRequest;
import iakka.platform.domain.comment.entity.Comment;
import iakka.platform.domain.comment.entity.Comment.CommentType;
import iakka.platform.domain.comment.repository.CommentRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public ResponseEntity<?> createComment(CommentRequest request) {
        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment parentComment = null;
        if (request.getParentCommentId() != null) {
            parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        }

        Comment comment = Comment.builder()
                .type(request.getType())
                .targetId(request.getTargetId())
                .content(request.getContent())
                .author(author)
                .parentComment(parentComment)
                .build();

        return ResponseEntity.ok(commentRepository.save(comment));
    }

    public ResponseEntity<List<Comment>> getComments(CommentType type, Long targetId) {
        return ResponseEntity.ok(commentRepository.findByTypeAndTargetIdAndParentCommentIsNull(type, targetId));
    }
}
