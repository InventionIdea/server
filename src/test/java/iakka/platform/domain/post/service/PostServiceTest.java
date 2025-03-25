package iakka.platform.domain.post.service;

import iakka.platform.domain.like.entity.Like.LikeType;
import iakka.platform.domain.like.repository.LikeRepository;
import iakka.platform.domain.post.dto.PostRequest;
import iakka.platform.domain.post.dto.PostResponse;
import iakka.platform.domain.post.entity.Post;
import iakka.platform.domain.post.repository.PostRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void 전체_게시글_조회() {
        List<Post> posts = List.of(new Post());
        when(postRepository.findAll()).thenReturn(posts);

        List<Post> result = postService.getAllPosts();
        assertEquals(1, result.size());
    }

    @Test
    void 게시글_생성() {
        PostRequest req = new PostRequest();
        req.setTitle("제목");
        req.setContent("내용");
        req.setAuthorId(1L);

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(postRepository.save(any())).thenReturn(new Post());

        ResponseEntity<?> response = postService.createPost(req);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void 게시글_단건_조회_및_조회수_증가_및_좋아요수포함() {
        // given
        Long postId = 1L;

        User author = new User();
        author.setId(1L);
        author.setUsername("작성자");

        Post post = new Post();
        post.setId(postId);
        post.setTitle("테스트 제목");
        post.setContent("테스트 내용");
        post.setAuthor(author);
        post.setViews(5);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(likeRepository.countByTypeAndTargetId(LikeType.POST, postId)).thenReturn(3L);

        // when
        PostResponse response = postService.viewPost(postId);

        // then
        assertEquals(6, post.getViews()); // 조회수 1 증가했는지
        assertEquals("테스트 제목", response.getTitle());
        assertEquals(3L, response.getLikeCount());
        assertEquals("작성자", response.getAuthorName());
    }
}
