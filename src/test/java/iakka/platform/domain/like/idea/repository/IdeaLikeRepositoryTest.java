package iakka.platform.domain.like.idea.repository;

import iakka.platform.domain.idea.entity.Idea;
import iakka.platform.domain.like.idea.entity.IdeaLike;
import iakka.platform.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class IdeaLikeRepositoryTest {

    @Autowired
    private IdeaLikeRepository ideaLikeRepository;

    @Test
    void 아이디어와_유저로_조회() {
        Idea idea = new Idea("user1", "아이디어 제목", "fileId");
        User user = new User();
        user.setUsername("테스트유저");

        IdeaLike like = new IdeaLike(idea, user);
        ideaLikeRepository.save(like);

        Optional<IdeaLike> result = ideaLikeRepository.findByIdeaAndUser(idea, user);
        assertTrue(result.isPresent());
    }
}
