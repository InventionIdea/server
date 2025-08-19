package iakka.platform.domain.idea.repository;

import iakka.platform.domain.idea.entity.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {
    List<Idea> findByUserId(String userId);
    List<Idea> findByUserIdAndTitle(String userId, String title);
}