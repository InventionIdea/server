package iakka.platform.domain.crew.repository;

import iakka.platform.domain.crew.entity.Crew;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CrewRepositoryTest {

    @Autowired
    private CrewRepository crewRepository;

    @Test
    void 크루_저장_및_조회() {
        Crew crew = new Crew();
        crew.setName("테스트크루");
        crew.setDescription("설명");

        Crew saved = crewRepository.save(crew);
        Optional<Crew> found = crewRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("테스트크루", found.get().getName());
    }
}
