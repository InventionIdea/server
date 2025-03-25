package iakka.platform.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import iakka.platform.domain.comment.dto.CommentRequest;
import iakka.platform.domain.comment.entity.Comment.CommentType;
import iakka.platform.domain.comment.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("댓글 작성 API 테스트")
    void createComment() throws Exception {
        CommentRequest request = new CommentRequest();
        request.setType(CommentType.IDEA);
        request.setTargetId(10L);
        request.setAuthorId(1L);
        request.setContent("댓글입니다.");

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(commentService, times(1)).createComment(any(CommentRequest.class));
    }

    @Test
    @DisplayName("댓글 조회 API 테스트")
    void getComments() throws Exception {
        mockMvc.perform(get("/comments/POST/77"))
                .andExpect(status().isOk());

        verify(commentService, times(1)).getComments(CommentType.POST, 77L);
    }
}
