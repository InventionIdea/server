package iakka.platform.domain.like.service;

import iakka.platform.domain.like.entity.PostLike;
import iakka.platform.domain.like.repository.PostLikeRepository;
import iakka.platform.domain.post.entity.Post;
import iakka.platform.domain.post.repository.PostRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostLikeService(PostLikeRepository postLikeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (postLikeRepository.findByPostAndUser(post, user).isPresent()) {
            return ResponseEntity.badRequest().body("User already liked this post");
        }

        PostLike postLike = new PostLike(post, user);
        postLikeRepository.save(postLike);

        return ResponseEntity.ok("Post liked successfully");
    }

    public ResponseEntity<?> unlikePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        postLikeRepository.findByPostAndUser(post, user)
                .ifPresent(postLikeRepository::delete);

        return ResponseEntity.ok("Post unliked successfully");
    }

    public long getLikeCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return postLikeRepository.countByPost(post);
    }
}
