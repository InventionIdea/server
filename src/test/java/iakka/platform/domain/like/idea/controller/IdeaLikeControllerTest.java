package iakka.platform.domain.like.idea.controller;

import iakka.platform.domain.like.idea.service.IdeaLikeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IdeaLikeController.class)
class IdeaLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdeaLikeService ideaLikeService;

    @Test
    void 아이디어_좋아요_요청() throws Exception {
        mockMvc.perform(post("/idea-likes/1")
                        .param("userId", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void 아이디어_좋아요_취소_요청() throws Exception {
        mockMvc.perform(delete("/idea-likes/1")
                        .param("userId", "2"))
                .andExpect(status().isOk());
    }
}
