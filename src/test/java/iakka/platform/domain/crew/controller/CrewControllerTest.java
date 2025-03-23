package iakka.platform.domain.crew.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import iakka.platform.domain.crew.dto.CrewRequest;
import iakka.platform.domain.crew.entity.Crew;
import iakka.platform.domain.crew.service.CrewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CrewController.class)
class CrewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrewService crewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 크루_생성_요청() throws Exception {
        CrewRequest request = new CrewRequest();
        request.setName("테스트 크루");
        request.setDescription("테스트 설명");

        Crew responseCrew = new Crew();
        responseCrew.setName("테스트 크루");
        responseCrew.setDescription("테스트 설명");

        when(crewService.createCrew(any(CrewRequest.class))).thenReturn(responseCrew);

        mockMvc.perform(post("/crews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("테스트 크루"))
                .andExpect(jsonPath("$.description").value("테스트 설명"));
    }

    @Test
    void 크루_가입_요청() throws Exception {
        doNothing().when(crewService).joinCrew(1L, 2L);

        mockMvc.perform(post("/crews/1/join/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Joined the crew successfully!"));
    }
}
