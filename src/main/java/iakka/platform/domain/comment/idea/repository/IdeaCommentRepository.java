package iakka.platform.domain.comment.idea.repository;

import iakka.platform.domain.comment.idea.entity.IdeaComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IdeaCommentRepository extends JpaRepository<IdeaComment, Long> {
    List<IdeaComment> findByIdeaIdAndParentCommentIsNull(Long ideaId);
    List<IdeaComment> findByParentCommentId(Long parentId);
}
