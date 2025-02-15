package iakka.platform.post;

import iakka.platform.user.User;
import iakka.platform.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest) {
        if (postRequest.getAuthorId() == null) {
            return ResponseEntity.badRequest().body("Error: authorId is required");
        }

        User author = userRepository.findById(postRequest.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + postRequest.getAuthorId()));

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setLikes(0);
        post.setAuthor(author); // 👈 필수 값 추가!

        return ResponseEntity.ok(postRepository.save(post));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Post> likePost(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setLikes(post.getLikes() + 1);
        return ResponseEntity.ok(postRepository.save(post));
    }
}

class PostRequest {
    private String title;
    private String content;
    private Long authorId;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
}