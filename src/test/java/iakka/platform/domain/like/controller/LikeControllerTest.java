package iakka.platform.domain.like.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import iakka.platform.domain.like.entity.Like.LikeType;
import iakka.platform.domain.like.service.LikeService;
import iakka.platform.domain.user.entity.User;
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

@WebMvcTest(LikeController.class)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @Autowired
    private ObjectMapper objectMapper;

    private final User testUser = new User();

    @Test
    @DisplayName("좋아요 등록 API")
    void like() throws Exception {
        testUser.setId(1L);

        mockMvc.perform(post("/likes/POST/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());

        verify(likeService, times(1)).like(LikeType.POST, 101L, testUser);
    }

    @Test
    @DisplayName("좋아요 해제 API")
    void unlike() throws Exception {
        testUser.setId(2L);

        mockMvc.perform(delete("/likes/IDEA/202")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());

        verify(likeService, times(1)).unlike(LikeType.IDEA, 202L, testUser);
    }

    @Test
    @DisplayName("좋아요 여부 확인 API")
    void isLiked() throws Exception {
        testUser.setId(3L);
        when(likeService.isLiked(LikeType.IDEA, 404L, testUser)).thenReturn(true);

        mockMvc.perform(get("/likes/IDEA/404/liked")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("좋아요 수 API")
    void countLikes() throws Exception {
        when(likeService.countLikes(LikeType.POST, 888L)).thenReturn(12L);

        mockMvc.perform(get("/likes/POST/888/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("12"));
    }
}
