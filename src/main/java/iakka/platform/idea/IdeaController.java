package iakka.platform.idea;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;

@RestController
@RequestMapping("/ideas")
public class IdeaController {

    private final IdeaService ideaService;

    public IdeaController(IdeaService ideaService) {
        this.ideaService = ideaService;
    }

    @PostMapping("/generate")
    public Mono<Idea> generateVideo(@RequestBody IdeaRequest request) {
        return ideaService.generateVideo(
                request.getUserId(),
                request.getTitle(),
                request.getScript()
        );
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

    public static class IdeaRequest {
        private String userId;
        private String title;
        private List<String> script;

        public IdeaRequest() {}

        public IdeaRequest(String userId, String title, List<String> script) {
            this.userId = userId;
            this.title = title;
            this.script = script;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getScript() {
            return script;
        }

        public void setScript(List<String> script) {
            this.script = script;
        }
    }

    public static class FileUpdateRequest {
        @JsonProperty("user_id")
        private String userId;

        private String title;

        @JsonProperty("file_id")
        private String fileId;

        public FileUpdateRequest() {}

        public FileUpdateRequest(String userId, String title, String fileId) {
            this.userId = userId;
            this.title = title;
            this.fileId = fileId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }
    }
}