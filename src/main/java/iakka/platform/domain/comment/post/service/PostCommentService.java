package iakka.platform.domain.comment.post.service;

import iakka.platform.domain.comment.post.dto.PostCommentRequest;
import iakka.platform.domain.comment.post.entity.PostComment;
import iakka.platform.domain.comment.post.repository.PostCommentRepository;
import iakka.platform.domain.post.entity.Post;
import iakka.platform.domain.post.repository.PostRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCommentService {
    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostCommentService(PostCommentRepository postCommentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.postCommentRepository = postCommentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> createComment(PostCommentRequest request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        PostComment parentComment = null;
        if (request.getParentCommentId() != null) {
            parentComment = postCommentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        }

        PostComment comment = new PostComment();
        comment.setContent(request.getContent());
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setParentComment(parentComment);

        return ResponseEntity.ok(postCommentRepository.save(comment));
    }

    public ResponseEntity<List<PostComment>> getComments(Long postId) {
        return ResponseEntity.ok(postCommentRepository.findByPostIdAndParentCommentIsNull(postId));
    }
}
