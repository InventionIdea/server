package iakka.platform.idea;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {
    List<Idea> findByUserId(String userId);
}