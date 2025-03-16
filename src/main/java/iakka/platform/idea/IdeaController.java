package iakka.platform.idea;

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

}