package iakka.platform.domain.like.service;

import iakka.platform.domain.like.entity.IdeaLike;
import iakka.platform.domain.like.repository.IdeaLikeRepository;
import iakka.platform.domain.idea.entity.Idea;
import iakka.platform.domain.idea.repository.IdeaRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class IdeaLikeService {

    private final IdeaLikeRepository ideaLikeRepository;
    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;

    public IdeaLikeService(IdeaLikeRepository ideaLikeRepository, IdeaRepository ideaRepository, UserRepository userRepository) {
        this.ideaLikeRepository = ideaLikeRepository;
        this.ideaRepository = ideaRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> likeIdea(Long ideaId, Long userId) {
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new RuntimeException("Idea not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (ideaLikeRepository.findByIdeaAndUser(idea, user).isPresent()) {
            return ResponseEntity.badRequest().body("User already liked this idea");
        }

        IdeaLike ideaLike = new IdeaLike(idea, user);
        ideaLikeRepository.save(ideaLike);

        return ResponseEntity.ok("Idea liked successfully");
    }

    public ResponseEntity<?> unlikeIdea(Long ideaId, Long userId) {
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new RuntimeException("Idea not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ideaLikeRepository.findByIdeaAndUser(idea, user)
                .ifPresent(ideaLikeRepository::delete);

        return ResponseEntity.ok("Idea unliked successfully");
    }

    public long getLikeCount(Long ideaId) {
        Idea idea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> new RuntimeException("Idea not found"));
        return ideaLikeRepository.countByIdea(idea);
    }
}
