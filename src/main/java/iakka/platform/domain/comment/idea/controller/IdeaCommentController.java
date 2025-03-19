package iakka.platform.domain.comment.idea.controller;

import iakka.platform.domain.comment.idea.dto.IdeaCommentRequest;
import iakka.platform.domain.comment.idea.service.IdeaCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/idea-comments")
public class IdeaCommentController {
    private final IdeaCommentService ideaCommentService;

    public IdeaCommentController(IdeaCommentService ideaCommentService) {
        this.ideaCommentService = ideaCommentService;
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody IdeaCommentRequest request) {
        return ideaCommentService.createComment(request);
    }

    @GetMapping("/{ideaId}")
    public ResponseEntity<?> getComments(@PathVariable Long ideaId) {
        return ideaCommentService.getComments(ideaId);
    }
}
