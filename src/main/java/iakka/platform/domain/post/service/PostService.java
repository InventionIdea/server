package iakka.platform.domain.post.service;

import iakka.platform.domain.post.dto.PostRequest;
import iakka.platform.domain.post.entity.Post;
import iakka.platform.domain.post.repository.PostRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public ResponseEntity<List<Post>> getPostsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(postRepository.findByAuthorId(userId));
    }

    public ResponseEntity<?> createPost(PostRequest postRequest) {
        if (postRequest.getAuthorId() == null) {
            return ResponseEntity.badRequest().body("Error: authorId is required");
        }

        User author = userRepository.findById(postRequest.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + postRequest.getAuthorId()));

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthor(author);

        return ResponseEntity.ok(postRepository.save(post));
    }

    public ResponseEntity<?> updatePost(Long id, PostRequest postRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getAuthor().getId().equals(postRequest.getAuthorId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: Only the author can update this post");
        }

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        return ResponseEntity.ok(postRepository.save(post));
    }

    public ResponseEntity<?> deletePost(Long id, Long authorId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getAuthor().getId().equals(authorId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error: Only the author can delete this post");
        }

        postRepository.delete(post);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<List<Post>> searchPosts(String keyword) {
        List<Post> posts = postRepository.searchByKeyword(keyword);
        return ResponseEntity.ok(posts);
    }
}
