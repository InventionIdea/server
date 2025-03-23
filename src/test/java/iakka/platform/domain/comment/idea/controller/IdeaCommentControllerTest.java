package iakka.platform.domain.comment.idea.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import iakka.platform.domain.comment.idea.dto.IdeaCommentRequest;
import iakka.platform.domain.comment.idea.service.IdeaCommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IdeaCommentController.class)
class IdeaCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdeaCommentService ideaCommentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 아이디어_댓글_등록_요청() throws Exception {
        IdeaCommentRequest request = new IdeaCommentRequest();
        request.setAuthorId(1L);
        request.setIdeaId(100L);
        request.setContent("댓글입니다");

        mockMvc.perform(post("/idea-comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
