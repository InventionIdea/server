package iakka.platform.domain.like.repository;

import iakka.platform.domain.like.entity.IdeaLike;
import iakka.platform.domain.idea.entity.Idea;
import iakka.platform.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdeaLikeRepository extends JpaRepository<IdeaLike, Long> {
    Optional<IdeaLike> findByIdeaAndUser(Idea idea, User user);
    long countByIdea(Idea idea);
    void deleteByIdeaAndUser(Idea idea, User user);
}
