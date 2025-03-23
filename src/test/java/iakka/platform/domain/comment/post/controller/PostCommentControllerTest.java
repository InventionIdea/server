package iakka.platform.domain.comment.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import iakka.platform.domain.comment.post.dto.PostCommentRequest;
import iakka.platform.domain.comment.post.service.PostCommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostCommentController.class)
class PostCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostCommentService postCommentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 댓글_등록_요청() throws Exception {
        PostCommentRequest request = new PostCommentRequest();
        request.setAuthorId(1L);
        request.setPostId(100L);
        request.setContent("댓글 내용입니다.");

        mockMvc.perform(post("/post-comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
