package iakka.platform.domain.like.controller;

import iakka.platform.domain.like.service.IdeaLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/idea-likes")
public class IdeaLikeController {

    private final IdeaLikeService ideaLikeService;

    public IdeaLikeController(IdeaLikeService ideaLikeService) {
        this.ideaLikeService = ideaLikeService;
    }

    @PostMapping("/{ideaId}")
    public ResponseEntity<?> likeIdea(@PathVariable Long ideaId, @RequestParam Long userId) {
        return ideaLikeService.likeIdea(ideaId, userId);
    }

    @DeleteMapping("/{ideaId}")
    public ResponseEntity<?> unlikeIdea(@PathVariable Long ideaId, @RequestParam Long userId) {
        return ideaLikeService.unlikeIdea(ideaId, userId);
    }

    @GetMapping("/{ideaId}/count")
    public ResponseEntity<Long> getLikeCount(@PathVariable Long ideaId) {
        return ResponseEntity.ok(ideaLikeService.getLikeCount(ideaId));
    }
}
