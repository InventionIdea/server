package iakka.platform.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import iakka.platform.domain.comment.dto.CommentRequest;
import iakka.platform.domain.comment.entity.Comment.CommentType;
import iakka.platform.domain.comment.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @Test
    @DisplayName("댓글 작성 API 테스트")
    void createComment() throws Exception {
        CommentRequest request = new CommentRequest();
        request.setType(CommentType.IDEA);
        request.setTargetId(10L);
        request.setAuthorId(1L);
        request.setContent("댓글입니다.");

        doReturn(ResponseEntity.ok("작성 완료"))
                .when(commentService).createComment(any());

        mockMvc.perform(post("/comments")
                        .with(csrf())
                        .with(user("tester").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(commentService, times(1)).createComment(any(CommentRequest.class));
    }

    @Test
    @DisplayName("댓글 조회 API 테스트")
    void getComments() throws Exception {
        doReturn(ResponseEntity.ok().build())
                .when(commentService).getComments(CommentType.POST, 77L);

        mockMvc.perform(get("/comments/POST/77")
                        .with(user("tester").roles("USER")))
                .andExpect(status().isOk());

        verify(commentService, times(1)).getComments(CommentType.POST, 77L);
    }

    @Test
    @DisplayName("댓글 수정 API 테스트")
    void updateComment() throws Exception {
        CommentRequest request = new CommentRequest();
        request.setType(CommentType.POST);
        request.setTargetId(10L);
        request.setAuthorId(1L);
        request.setContent("수정된 댓글입니다.");

        doReturn(ResponseEntity.ok("수정 완료"))
                .when(commentService).updateComment(eq(1L), any(CommentRequest.class));

        mockMvc.perform(put("/comments/1")
                        .with(csrf())
                        .with(user("tester").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("수정 완료"));

        verify(commentService, times(1)).updateComment(eq(1L), any(CommentRequest.class));
    }

    @Test
    @DisplayName("댓글 삭제 API 테스트")
    void deleteComment() throws Exception {
        doReturn(ResponseEntity.ok("삭제 완료"))
                .when(commentService).deleteComment(1L);

        mockMvc.perform(delete("/comments/1")
                        .with(csrf())
                        .with(user("tester").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().string("삭제 완료"));

        verify(commentService, times(1)).deleteComment(1L);
    }
}
