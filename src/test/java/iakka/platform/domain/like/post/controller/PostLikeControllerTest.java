package iakka.platform.domain.like.post.controller;

import iakka.platform.domain.like.post.service.PostLikeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostLikeController.class)
class PostLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostLikeService postLikeService;

    @Test
    void 좋아요_요청() throws Exception {
        mockMvc.perform(post("/likes/1")
                        .param("userId", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void 좋아요_취소_요청() throws Exception {
        mockMvc.perform(delete("/likes/1")
                        .param("userId", "2"))
                .andExpect(status().isOk());
    }
}
