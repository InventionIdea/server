package iakka.platform.domain.comment.idea.service;

import iakka.platform.domain.comment.idea.dto.IdeaCommentRequest;
import iakka.platform.domain.comment.idea.entity.IdeaComment;
import iakka.platform.domain.comment.idea.repository.IdeaCommentRepository;
import iakka.platform.domain.idea.entity.Idea;
import iakka.platform.domain.idea.repository.IdeaRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class IdeaCommentServiceTest {

    private IdeaCommentRepository commentRepository;
    private IdeaRepository ideaRepository;
    private UserRepository userRepository;
    private IdeaCommentService ideaCommentService;

    @BeforeEach
    void 설정() {
        commentRepository = mock(IdeaCommentRepository.class);
        ideaRepository = mock(IdeaRepository.class);
        userRepository = mock(UserRepository.class);
        ideaCommentService = new IdeaCommentService(commentRepository, ideaRepository, userRepository);
    }

    @Test
    void 댓글_등록() {
        IdeaCommentRequest request = new IdeaCommentRequest();
        request.setAuthorId(1L);
        request.setIdeaId(10L);
        request.setContent("아이디어 댓글");

        Idea idea = new Idea("user1", "제목", "file");
        User user = new User();
        user.setId(1L);

        when(ideaRepository.findById(10L)).thenReturn(Optional.of(idea));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        ideaCommentService.createComment(request);

        verify(commentRepository, times(1)).save(any(IdeaComment.class));
    }
}
