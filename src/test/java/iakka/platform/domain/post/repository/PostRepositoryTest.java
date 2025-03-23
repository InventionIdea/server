package iakka.platform.domain.post.repository;

import iakka.platform.domain.post.entity.Post;
import iakka.platform.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private PostRepository postRepository;

    @Test
    void 작성자ID_기반_게시글_조회() {
        User user = new User();
        user.setId(1L);
        em.persist(user);

        Post post = new Post();
        post.setAuthor(user);
        em.persist(post);

        List<Post> result = postRepository.findByAuthorId(1L);
        assertFalse(result.isEmpty());
    }

    @Test
    void 키워드_기반_검색() {
        Post post = new Post();
        post.setTitle("hello world");
        post.setContent("test content");
        em.persist(post);

        List<Post> result = postRepository.searchByKeyword("hello");
        assertFalse(result.isEmpty());
    }
}
