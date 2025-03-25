package iakka.platform.domain.comment.service;

import iakka.platform.domain.comment.dto.CommentRequest;
import iakka.platform.domain.comment.entity.Comment;
import iakka.platform.domain.comment.entity.Comment.CommentType;
import iakka.platform.domain.comment.repository.CommentRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock private CommentRepository commentRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("댓글 생성 로직 테스트")
    void createComment_shouldSave() {
        CommentRequest request = new CommentRequest();
        request.setAuthorId(1L);
        request.setType(CommentType.POST);
        request.setTargetId(123L);
        request.setContent("Mock 댓글");

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = commentService.createComment(request);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
}
