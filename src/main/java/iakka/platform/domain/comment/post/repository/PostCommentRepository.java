package iakka.platform.domain.comment.post.repository;

import iakka.platform.domain.comment.post.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    List<PostComment> findByPostIdAndParentCommentIsNull(Long postId);
    List<PostComment> findByParentCommentId(Long parentId);
}