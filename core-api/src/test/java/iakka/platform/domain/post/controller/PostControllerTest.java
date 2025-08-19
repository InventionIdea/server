package iakka.platform.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import iakka.platform.domain.post.dto.PostRequest;
import iakka.platform.domain.post.dto.PostResponse;
import iakka.platform.domain.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 전체_게시글_조회() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk());
    }

    @Test
    void 게시글_생성() throws Exception {
        PostRequest postRequest = new PostRequest();
        postRequest.setTitle("테스트 제목");
        postRequest.setContent("테스트 내용");
        postRequest.setAuthorId(1L);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void 게시글_단건_조회() throws Exception {
        Long postId = 1L;

        PostResponse response = PostResponse.builder()
                .id(postId)
                .title("테스트 제목")
                .content("테스트 내용")
                .authorName("작성자")
                .views(10)
                .likeCount(5)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(postService.viewPost(postId)).thenReturn(response);

        mockMvc.perform(get("/posts/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("테스트 제목"))
                .andExpect(jsonPath("$.views").value(10))
                .andExpect(jsonPath("$.likeCount").value(5));
    }
}
