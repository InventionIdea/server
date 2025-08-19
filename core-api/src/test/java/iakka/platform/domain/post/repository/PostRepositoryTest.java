package iakka.platform.domain.post.repository;

import iakka.platform.domain.post.entity.Post;
import iakka.platform.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
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
        em.persist(user);

        Post post = new Post();
        post.setAuthor(user);
        post.setTitle("제목1");
        post.setContent("내용1");
        em.persist(post);

        List<Post> result = postRepository.findByAuthorId(user.getId());
        assertFalse(result.isEmpty());
        assertEquals("제목1", result.get(0).getTitle());
    }

    @Test
    void 키워드_기반_검색() {
        Post post = new Post();
        post.setTitle("hello world");
        post.setContent("test content");
        em.persist(post);

        List<Post> result = postRepository.searchByKeyword("hello");
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getTitle().contains("hello"));
    }

    @Test
    void 게시글_생성시_기본값_설정() {
        User user = new User();
        em.persist(user);

        Post post = new Post();
        post.setAuthor(user);
        post.setTitle("test");
        post.setContent("content");
        em.persist(post);

        Post saved = postRepository.findById(post.getId()).orElseThrow();

        assertNotNull(saved.getCreatedAt(), "createdAt은 null이 아니어야 함");
        assertNotNull(saved.getUpdatedAt(), "updatedAt은 null이 아니어야 함");
        assertEquals(0, saved.getViews(), "초기 조회수는 0이어야 함");
    }

    @Test
    void 게시글_수정시_updatedAt_갱신() {
        User user = new User();
        em.persist(user);

        Post post = new Post();
        post.setAuthor(user);
        post.setTitle("original");
        post.setContent("original");
        em.persist(post);

        em.flush(); // 영속성 컨텍스트 반영
        em.clear(); // 캐시 제거

        Post toUpdate = postRepository.findById(post.getId()).orElseThrow();
        LocalDateTime originalUpdatedAt = toUpdate.getUpdatedAt();

        toUpdate.setContent("modified");
        em.persist(toUpdate);
        em.flush();
        em.clear();

        Post updated = postRepository.findById(post.getId()).orElseThrow();

        assertTrue(updated.getUpdatedAt().isAfter(originalUpdatedAt), "updatedAt이 갱신되어야 함");
    }
}
