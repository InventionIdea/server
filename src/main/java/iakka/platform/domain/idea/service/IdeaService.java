package iakka.platform.domain.idea.service;

import iakka.platform.domain.idea.entity.Idea;
import iakka.platform.domain.idea.repository.IdeaRepository;
import iakka.platform.domain.idea.dto.IdeaRequest;
import iakka.platform.domain.idea.dto.IdeaResponse;
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
        Idea idea = new Idea(userId, title, null);
        ideaRepository.save(idea);

        return webClient.post()
                .uri("/generate-video")
                .bodyValue(new IdeaRequest(userId, title, script))
                .retrieve()
                .bodyToMono(IdeaResponse.class)
                .doOnNext(response -> {
                    idea.setFileId(response.getFileId());
                    ideaRepository.save(idea);
                })
                .map(response -> idea);
    }


    public boolean updateFileId(String userId, String title, String fileId) {
        List<Idea> ideas = ideaRepository.findByUserIdAndTitle(userId, title);
        if (!ideas.isEmpty()) {
            ideas.forEach(idea -> {
                idea.setFileId(fileId);
                ideaRepository.save(idea);
            });
            return true;
        }
        return false;
    }
}
