package iakka.platform.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글(postId)에 대한 최상위 댓글(부모가 없는 댓글) 조회
    List<Comment> findByPostIdAndParentCommentIsNull(Long postId);

    // 특정 부모 댓글(parentId)에 대한 대댓글 조회
    List<Comment> findByParentCommentId(Long parentId);

    // 특정 댓글 및 해당 대댓글을 삭제
    @Transactional
    void deleteAllByParentCommentId(Long parentId);
}