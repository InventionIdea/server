package iakka.platform.domain.idea.controller;

import iakka.platform.domain.idea.service.IdeaService;
import iakka.platform.domain.idea.entity.Idea;
import iakka.platform.domain.idea.dto.IdeaRequest;
import iakka.platform.domain.idea.dto.FileUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ideas")
public class IdeaController {

    private final IdeaService ideaService;

    public IdeaController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Idea> generateVideo(@RequestBody IdeaRequest request) {
        try {
            Idea idea = ideaService.generateVideo(
                    request.getUserId(),
                    request.getTitle(),
                    request.getScript()
            );
            return ResponseEntity.ok(idea);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/list/{userId}")
    public List<Idea> getUserIdeas(@PathVariable String userId) {
        return ideaService.getIdeasByUserId(userId);
    }

    @PostMapping("/update-file-id")
    public ResponseEntity<String> updateFileId(@RequestBody FileUpdateRequest request) {
        boolean updated = ideaService.updateFileId(request.getUserId(), request.getTitle(), request.getFileId());
        if (updated) {
            return ResponseEntity.ok("File ID updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update File ID.");
        }
    }

    @DeleteMapping("/{ideaId}")
    public ResponseEntity<String> deleteIdea(@PathVariable Long ideaId) {
        boolean deleted = ideaService.deleteIdeaById(ideaId);
        if (deleted) {
            return ResponseEntity.ok("Idea deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
