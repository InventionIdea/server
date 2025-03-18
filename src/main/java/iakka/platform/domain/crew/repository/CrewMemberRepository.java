package iakka.platform.domain.crew.repository;

import iakka.platform.domain.crew.entity.CrewMember;
import iakka.platform.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {
    int countByUser(User user);
}
