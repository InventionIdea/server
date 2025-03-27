import iakka.platform.PlatformApplication;
import iakka.platform.domain.crew.entity.Crew;
import iakka.platform.domain.crew.entity.CrewMember;
import iakka.platform.domain.crew.repository.CrewMemberRepository;
import iakka.platform.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = PlatformApplication.class)
class CrewMemberRepositoryTest {

    @Autowired
    private CrewMemberRepository crewMemberRepository;

    @Autowired
    private TestEntityManager em; // JPA entity 저장용

    @Test
    void 사용자_속한_크루_수_조회() {
        User user = new User();
        user.setUsername("tester");
        user.setPassword("testpass");
        user.setUserId("tester01");
        user.setPhoneNumber("010-1234-5678");
        user.setRealName("테스트유저");
        user.setPoints(0);
        em.persist(user);

        Crew crew1 = new Crew();
        crew1.setName("크루1");
        crew1.setDescription("첫 번째 크루");
        em.persist(crew1);

        Crew crew2 = new Crew();
        crew2.setName("크루2");
        crew2.setDescription("두 번째 크루");
        em.persist(crew2);

        CrewMember member1 = new CrewMember();
        member1.setUser(user);
        member1.setCrew(crew1);

        CrewMember member2 = new CrewMember();
        member2.setUser(user);
        member2.setCrew(crew2);

        em.persist(member1);
        em.persist(member2);

        int crewCount = crewMemberRepository.countByUser(user);

        assertEquals(2, crewCount);
    }

    @Test
    @DisplayName("사용자와 크루로 CrewMember 찾기 - 존재하는 경우")
    void findByUserAndCrew_존재() {
        User user = new User();
        user.setUsername("tester");
        user.setPassword("testpass");
        user.setUserId("tester01");
        user.setPhoneNumber("010-1234-5678");
        user.setRealName("테스트유저");
        user.setPoints(0);
        em.persist(user);

        Crew crew = new Crew();
        crew.setName("Crew A");
        em.persist(crew);

        CrewMember member = new CrewMember();
        member.setUser(user);
        member.setCrew(crew);
        em.persist(member);

        Optional<CrewMember> result = crewMemberRepository.findByUserAndCrew(user, crew);
        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().getUser().getUsername());
    }

    @Test
    @DisplayName("사용자와 크루로 CrewMember 찾기 - 존재하지 않는 경우")
    void findByUserAndCrew_없음() {
        User user = new User();
        user.setUsername("tester");
        user.setPassword("testpass");
        user.setUserId("tester01");
        user.setPhoneNumber("010-1234-5678");
        user.setRealName("테스트유저");
        user.setPoints(0);
        em.persist(user);

        Crew crew = new Crew();
        crew.setName("Crew A");
        em.persist(crew);

        Optional<CrewMember> result = crewMemberRepository.findByUserAndCrew(user, crew);
        assertFalse(result.isPresent());
    }
}
