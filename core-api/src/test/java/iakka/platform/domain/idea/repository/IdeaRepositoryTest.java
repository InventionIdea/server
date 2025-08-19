package iakka.platform.domain.idea.repository;

import iakka.platform.domain.idea.entity.Idea;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class IdeaRepositoryTest {

    @Autowired
    private IdeaRepository ideaRepository;

    @Test
    @DisplayName("유저ID로_아이디어_조회")
    void 유저ID로_아이디어_조회() {
        // given
        Idea idea1 = new Idea("user1", "아이디어1", "file-001");
        Idea idea2 = new Idea("user1", "아이디어2", "file-002");
        Idea idea3 = new Idea("user2", "다른유저_아이디어", "file-003");

        ideaRepository.saveAll(List.of(idea1, idea2, idea3));

        // when
        List<Idea> result = ideaRepository.findByUserId("user1");

        // then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(idea -> idea.getUserId().equals("user1")));
    }
}
