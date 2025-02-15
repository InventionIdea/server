package iakka.platform.controller;

import iakka.platform.model.Post;
import iakka.platform.repository.PostRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postRepository.save(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post postDetails) {
        return postRepository.findById(id)
                .map(post -> {
                    post.setTitle(postDetails.getTitle());
                    post.setContent(postDetails.getContent());
                    return ResponseEntity.ok(postRepository.save(post));
                }).orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable Long id) {
        return postRepository.findById(id)
                .map(post -> {
                    postRepository.delete(post);
                    return ResponseEntity.noContent().build();
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }


@PostMapping("/{id}/like")
public ResponseEntity<Post> likePost(@PathVariable Long id) {
    return postRepository.findById(id)
            .map(post -> {
                post.setLikes(post.getLikes() + 1);
                return ResponseEntity.ok(postRepository.save(post));
            }).orElse(ResponseEntity.notFound().build());
}
}
