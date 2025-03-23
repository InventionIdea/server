package iakka.platform.domain.crew.repository;

import iakka.platform.domain.crew.entity.Crew;
import iakka.platform.domain.crew.entity.CrewMember;
import iakka.platform.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CrewMemberRepositoryTest {

    @Autowired
    private CrewMemberRepository crewMemberRepository;

    @Test
    void 사용자_속한_크루_수_조회() {
        User user = new User();
        user.setUsername("테스트 유저");

        Crew crew1 = new Crew();
        crew1.setName("크루1");
        crew1.setDescription("첫 번째 크루");

        Crew crew2 = new Crew();
        crew2.setName("크루2");
        crew2.setDescription("두 번째 크루");

        CrewMember member1 = new CrewMember();
        member1.setUser(user);
        member1.setCrew(crew1);

        CrewMember member2 = new CrewMember();
        member2.setUser(user);
        member2.setCrew(crew2);

        // CrewMember 두 개를 DB에 저장
        crewMemberRepository.save(member1);
        crewMemberRepository.save(member2);

        // 사용자 속한 크루 수 조회
        int crewCount = crewMemberRepository.countByUser(user);

        assertEquals(2, crewCount);
    }
}
