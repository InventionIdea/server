package iakka.platform.domain.idea.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import iakka.platform.domain.idea.dto.FileUpdateRequest;
import iakka.platform.domain.idea.dto.IdeaRequest;
import iakka.platform.domain.idea.entity.Idea;
import iakka.platform.domain.idea.service.IdeaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IdeaController.class)
class IdeaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdeaService ideaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("아이디어_생성")
    void 아이디어_생성() throws Exception {
        IdeaRequest request = new IdeaRequest("user1", "타이틀", List.of("스크립트1", "스크립트2"));
        Idea mockIdea = new Idea("user1", "타이틀", "video-url");

        when(ideaService.generateVideo(any(), any(), any())).thenReturn(Mono.just(mockIdea));

        mockMvc.perform(post("/ideas/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.title").value("타이틀"))
                .andExpect(jsonPath("$.fileId").value("video-url"));
    }

    @Test
    @DisplayName("아이디어_목록_조회")
    void 아이디어_목록_조회() throws Exception {
        List<Idea> mockList = List.of(new Idea("user1", "테스트 제목", "file-1"));
        when(ideaService.getIdeasByUserId("user1")).thenReturn(mockList);

        mockMvc.perform(get("/ideas/list/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[0].title").value("테스트 제목"))
                .andExpect(jsonPath("$[0].fileId").value("file-1"));
    }

    @Test
    @DisplayName("파일ID_업데이트")
    void 파일ID_업데이트() throws Exception {
        FileUpdateRequest request = new FileUpdateRequest("user1", "타이틀", "file-999");
        when(ideaService.updateFileId("user1", "타이틀", "file-999")).thenReturn(true);

        mockMvc.perform(post("/ideas/update-file-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("File ID updated successfully."));
    }

    @Test
    @DisplayName("파일ID_업데이트_실패")
    void 파일ID_업데이트_실패() throws Exception {
        FileUpdateRequest request = new FileUpdateRequest("user1", "타이틀", "file-999");
        when(ideaService.updateFileId("user1", "타이틀", "file-999")).thenReturn(false);

        mockMvc.perform(post("/ideas/update-file-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Failed to update File ID."));
    }
}
