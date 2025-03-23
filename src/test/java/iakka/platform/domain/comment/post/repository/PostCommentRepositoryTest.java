package iakka.platform.domain.comment.post.repository;

import iakka.platform.domain.comment.post.entity.PostComment;
import iakka.platform.domain.post.entity.Post;
import iakka.platform.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostCommentRepositoryTest {

    @Autowired
    private PostCommentRepository repository;

    @Test
    void 게시글ID로_댓글_조회() {
        Post post = new Post();
        post.setTitle("게시글 제목");

        User user = new User();
        user.setUsername("댓글작성자");

        PostComment comment = new PostComment();
        comment.setPost(post);
        comment.setAuthor(user);
        comment.setContent("테스트 댓글");

        repository.save(comment);

        List<PostComment> result = repository.findByPostIdAndParentCommentIsNull(post.getId());

        assertTrue(result.size() >= 1);
    }
}
