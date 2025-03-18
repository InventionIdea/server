package iakka.platform.crew;

import iakka.platform.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {
    int countByUser(User user);
}
