package iakka.platform.domain.comment.idea.service;

import iakka.platform.domain.comment.idea.dto.IdeaCommentRequest;
import iakka.platform.domain.comment.idea.entity.IdeaComment;
import iakka.platform.domain.comment.idea.repository.IdeaCommentRepository;
import iakka.platform.domain.idea.entity.Idea;
import iakka.platform.domain.idea.repository.IdeaRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdeaCommentService {
    private final IdeaCommentRepository ideaCommentRepository;
    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;

    public IdeaCommentService(IdeaCommentRepository ideaCommentRepository, IdeaRepository ideaRepository, UserRepository userRepository) {
        this.ideaCommentRepository = ideaCommentRepository;
        this.ideaRepository = ideaRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> createComment(IdeaCommentRequest request) {
        Idea idea = ideaRepository.findById(request.getIdeaId())
                .orElseThrow(() -> new RuntimeException("Idea not found"));

        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        IdeaComment parentComment = null;
        if (request.getParentCommentId() != null) {
            parentComment = ideaCommentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        }

        IdeaComment comment = new IdeaComment();
        comment.setContent(request.getContent());
        comment.setIdea(idea);
        comment.setAuthor(author);
        comment.setParentComment(parentComment);

        return ResponseEntity.ok(ideaCommentRepository.save(comment));
    }

    public ResponseEntity<List<IdeaComment>> getComments(Long ideaId) {
        return ResponseEntity.ok(ideaCommentRepository.findByIdeaIdAndParentCommentIsNull(ideaId));
    }
}
