package iakka.platform.domain.like.post.repository;

import iakka.platform.domain.like.post.entity.PostLike;
import iakka.platform.domain.post.entity.Post;
import iakka.platform.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostLikeRepositoryTest {

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Test
    void 게시글과_유저로_조회() {
        Post post = new Post();
        post.setTitle("테스트 게시글");

        User user = new User();
        user.setUsername("홍길동");

        PostLike like = new PostLike(post, user);
        postLikeRepository.save(like);

        Optional<PostLike> result = postLikeRepository.findByPostAndUser(post, user);
        assertTrue(result.isPresent());
    }
}
