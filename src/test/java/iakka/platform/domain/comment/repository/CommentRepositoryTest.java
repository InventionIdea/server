package iakka.platform.domain.comment.repository;

import iakka.platform.domain.comment.entity.Comment;
import iakka.platform.domain.comment.entity.Comment.CommentType;
import iakka.platform.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("댓글 저장 및 조회 테스트")
    void saveAndFindByTypeAndTargetId() {
        User user = new User();
        user.setId(1L);

        Comment comment = Comment.builder()
                .type(CommentType.IDEA)
                .targetId(100L)
                .author(user)
                .content("테스트 댓글")
                .build();

        commentRepository.save(comment);

        List<Comment> result = commentRepository.findByTypeAndTargetIdAndParentCommentIsNull(CommentType.IDEA, 100L);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getContent()).isEqualTo("테스트 댓글");
    }
}
