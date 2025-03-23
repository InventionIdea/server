package iakka.platform.domain.like.idea.service;

import iakka.platform.domain.idea.entity.Idea;
import iakka.platform.domain.idea.repository.IdeaRepository;
import iakka.platform.domain.like.idea.entity.IdeaLike;
import iakka.platform.domain.like.idea.repository.IdeaLikeRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class IdeaLikeServiceTest {

    private IdeaLikeRepository ideaLikeRepository;
    private IdeaRepository ideaRepository;
    private UserRepository userRepository;
    private IdeaLikeService ideaLikeService;

    @BeforeEach
    void 설정() {
        ideaLikeRepository = mock(IdeaLikeRepository.class);
        ideaRepository = mock(IdeaRepository.class);
        userRepository = mock(UserRepository.class);
        ideaLikeService = new IdeaLikeService(ideaLikeRepository, ideaRepository, userRepository);
    }

    @Test
    void 아이디어_좋아요() {
        Idea idea = new Idea("user1", "테스트", "file");
        idea.setId(1L);
        User user = new User();
        user.setId(2L);

        when(ideaRepository.findById(1L)).thenReturn(Optional.of(idea));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(ideaLikeRepository.findByIdeaAndUser(idea, user)).thenReturn(Optional.empty());

        ideaLikeService.likeIdea(1L, 2L);

        verify(ideaLikeRepository, times(1)).save(any(IdeaLike.class));
    }

    @Test
    void 아이디어_좋아요_취소() {
        Idea idea = new Idea("user1", "테스트", "file");
        idea.setId(1L);
        User user = new User();
        user.setId(2L);

        when(ideaRepository.findById(1L)).thenReturn(Optional.of(idea));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(ideaLikeRepository.findByIdeaAndUser(idea, user)).thenReturn(Optional.of(new IdeaLike()));

        ideaLikeService.unlikeIdea(1L, 2L);

        verify(ideaLikeRepository, times(1)).deleteByIdeaAndUser(idea, user);
    }
}
