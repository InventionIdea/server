package iakka.platform.domain.crew.repository;

import iakka.platform.domain.crew.entity.Crew;
import iakka.platform.domain.crew.entity.CrewMember;
import iakka.platform.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {
    int countByUser(User user);
    Optional<CrewMember> findByUserAndCrew(User user, Crew crew);
}
