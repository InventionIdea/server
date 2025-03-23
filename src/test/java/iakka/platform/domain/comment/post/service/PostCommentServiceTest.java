package iakka.platform.domain.comment.post.service;

import iakka.platform.domain.comment.post.dto.PostCommentRequest;
import iakka.platform.domain.comment.post.entity.PostComment;
import iakka.platform.domain.comment.post.repository.PostCommentRepository;
import iakka.platform.domain.post.entity.Post;
import iakka.platform.domain.post.repository.PostRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

class PostCommentServiceTest {

    private PostCommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private PostCommentService postCommentService;

    @BeforeEach
    void 설정() {
        commentRepository = mock(PostCommentRepository.class);
        postRepository = mock(PostRepository.class);
        userRepository = mock(UserRepository.class);
        postCommentService = new PostCommentService(commentRepository, postRepository, userRepository);
    }

    @Test
    void 댓글_등록() {
        PostCommentRequest request = new PostCommentRequest();
        request.setAuthorId(1L);
        request.setPostId(10L);
        request.setContent("테스트 댓글");

        Post post = new Post();
        post.setId(10L);

        User user = new User();
        user.setId(1L);

        when(postRepository.findById(10L)).thenReturn(Optional.of(post));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        postCommentService.createComment(request);

        verify(commentRepository, times(1)).save(any(PostComment.class));
    }
}
