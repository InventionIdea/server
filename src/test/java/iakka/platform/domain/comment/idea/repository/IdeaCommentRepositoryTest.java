package iakka.platform.domain.comment.idea.repository;

import iakka.platform.domain.comment.idea.entity.IdeaComment;
import iakka.platform.domain.idea.entity.Idea;
import iakka.platform.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class IdeaCommentRepositoryTest {

    @Autowired
    private IdeaCommentRepository repository;

    @Test
    void 아이디어ID로_댓글_조회() {
        Idea idea = new Idea("user1", "테스트 아이디어", "file");
        User user = new User();
        user.setUsername("작성자");

        IdeaComment comment = new IdeaComment();
        comment.setIdea(idea);
        comment.setAuthor(user);
        comment.setContent("댓글 내용");

        repository.save(comment);

        List<IdeaComment> result = repository.findByIdeaIdAndParentCommentIsNull(idea.getId());

        assertTrue(result.size() >= 1);
    }
}
