package iakka.platform.domain.like.post.service;

import iakka.platform.domain.like.post.entity.PostLike;
import iakka.platform.domain.like.post.repository.PostLikeRepository;
import iakka.platform.domain.post.entity.Post;
import iakka.platform.domain.post.repository.PostRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostLikeServiceTest {

    private PostLikeRepository postLikeRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private PostLikeService postLikeService;

    @BeforeEach
    void 설정() {
        postLikeRepository = mock(PostLikeRepository.class);
        postRepository = mock(PostRepository.class);
        userRepository = mock(UserRepository.class);
        postLikeService = new PostLikeService(postLikeRepository, postRepository, userRepository);
    }

    @Test
    void 좋아요_추가() {
        Post post = new Post();
        post.setId(1L);
        User user = new User();
        user.setId(2L);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(postLikeRepository.findByPostAndUser(post, user)).thenReturn(Optional.empty());

        postLikeService.likePost(1L, 2L);

        verify(postLikeRepository, times(1)).save(any(PostLike.class));
    }

    @Test
    void 좋아요_취소() {
        Post post = new Post();
        post.setId(1L);
        User user = new User();
        user.setId(2L);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(postLikeRepository.findByPostAndUser(post, user)).thenReturn(Optional.of(new PostLike()));

        postLikeService.unlikePost(1L, 2L);

        verify(postLikeRepository, times(1)).deleteByPostAndUser(post, user);
    }
}
