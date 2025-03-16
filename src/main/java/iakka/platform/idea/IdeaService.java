package iakka.platform.idea;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final WebClient webClient;

    public IdeaService(IdeaRepository ideaRepository, WebClient webClient) {
        this.ideaRepository = ideaRepository;
        this.webClient = webClient;
    }

    public List<Idea> getIdeasByUserId(String userId) {
        return ideaRepository.findByUserId(userId);
    }

    public Mono<Idea> generateVideo(String userId, String title, List<String> script) {
        return webClient.post()
                .uri("/process-script/")
                .bodyValue(new IdeaRequest(userId, title, script))
                .retrieve()
                .bodyToMono(IdeaResponse.class)
                .map(response -> {
                    Idea idea = new Idea(userId, title, String.join(" ", script), response.getFileId());
                    return ideaRepository.save(idea);
                });
    }

    public class IdeaRequest {
        @JsonProperty("user_id")
        private String userId;

        private String title;

        private List<String> script;

        public IdeaRequest(String userId, String title, List<String> script) {
            this.userId = userId;
            this.title = title;
            this.script = script;
        }

        public String getUserId() {
            return userId;
        }

        public String getTitle() {
            return title;
        }

        public List<String> getScript() {
            return script;
        }
    }

    private static class IdeaResponse {
        private String fileId;

        public String getFileId() {
            return fileId;
        }
    }
}
