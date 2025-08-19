package iakka.platform.domain.crew.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import iakka.platform.domain.crew.dto.CrewRequest;
import iakka.platform.domain.crew.entity.Crew;
import iakka.platform.domain.crew.service.CrewService;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CrewController.class)
class CrewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CrewService crewService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUserId("tester");

        when(userRepository.findByUserId("tester"))
                .thenReturn(Optional.of(mockUser));
    }

    @Test
    @DisplayName("크루 생성 API 테스트")
    void 크루_생성_요청() throws Exception {
        CrewRequest request = new CrewRequest();
        request.setName("테스트 크루");
        request.setDescription("테스트 설명");

        Crew responseCrew = new Crew();
        responseCrew.setName("테스트 크루");
        responseCrew.setDescription("테스트 설명");

        when(crewService.createCrew(any(CrewRequest.class), anyLong())).thenReturn(responseCrew);

        mockMvc.perform(post("/crews")
                        .with(csrf())
                        .with(user("tester").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("테스트 크루"))
                .andExpect(jsonPath("$.description").value("테스트 설명"));
    }

    @Test
    @DisplayName("크루 가입 API 테스트")
    void 크루_가입_요청() throws Exception {
        doNothing().when(crewService).joinCrew(2L, 1L);

        mockMvc.perform(post("/crews/1/join/2")
                        .with(csrf())
                        .with(user("tester").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().string("Joined the crew successfully!"));

        verify(crewService, times(1)).joinCrew(2L, 1L);
    }

    @Test
    @DisplayName("크루 탈퇴 API 테스트")
    void 크루_탈퇴_요청() throws Exception {
        doNothing().when(crewService).leaveCrew(2L, 1L);

        mockMvc.perform(post("/crews/1/leave/2")
                        .with(csrf())
                        .with(user("tester").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().string("Left the crew successfully!"));

        verify(crewService, times(1)).leaveCrew(2L, 1L);
    }

    @Test
    @DisplayName("크루 수정 API 테스트")
    void 크루_수정_요청() throws Exception {
        CrewRequest request = new CrewRequest();
        request.setName("수정된 크루");
        request.setDescription("수정된 설명");

        Crew updatedCrew = new Crew();
        updatedCrew.setId(1L);
        updatedCrew.setName("수정된 크루");
        updatedCrew.setDescription("수정된 설명");

        when(crewService.updateCrew(eq(1L), any(CrewRequest.class))).thenReturn(updatedCrew);

        mockMvc.perform(put("/crews/1")
                        .with(csrf())
                        .with(user("tester").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("수정된 크루"))
                .andExpect(jsonPath("$.description").value("수정된 설명"));

        verify(crewService, times(1)).updateCrew(eq(1L), any(CrewRequest.class));
    }
}
