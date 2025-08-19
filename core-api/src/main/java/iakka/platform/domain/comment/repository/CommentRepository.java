package iakka.platform.domain.comment.repository;

import iakka.platform.domain.comment.entity.Comment;
import iakka.platform.domain.comment.entity.Comment.CommentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTypeAndTargetIdAndParentCommentIsNull(CommentType type, Long targetId);
    List<Comment> findByParentCommentId(Long parentId);
}
