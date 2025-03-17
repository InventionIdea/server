package iakka.platform.idea;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Optional;

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
        Idea idea = new Idea(userId, title, String.join(" ", script), null);
        ideaRepository.save(idea); // 먼저 DB에 저장 (file ID 없음)

        return webClient.post()
                .uri("/generate-video")
                .bodyValue(new IdeaRequest(userId, title, script))
                .retrieve()
                .bodyToMono(IdeaResponse.class)
                .doOnNext(response -> {
                    idea.setFileId(response.getFileId());
                    ideaRepository.save(idea); // 생성된 file ID 업데이트
                })
                .map(response -> idea);
    }

    public boolean updateFileId(String userId, String title, String fileId) {
        List<Idea> ideas = ideaRepository.findByUserIdAndTitle(userId, title);

        if (!ideas.isEmpty()) {
            for (Idea idea : ideas) {
                idea.setFileId(fileId);
                ideaRepository.save(idea);
            }
            return true;
        }
        return false;
    }

    public static class IdeaRequest {
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
        @JsonProperty("file_id")
        private String fileId;

        public String getFileId() {
            return fileId;
        }
    }
}