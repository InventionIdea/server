package iakka.platform.domain.user.controller;

import iakka.platform.domain.user.service.UserPointService;
import iakka.platform.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserPointService userPointService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void addPoints_Success() throws Exception {
        // Given
        Long userId = 1L;
        int amount = 50;

        // When & Then
        mockMvc.perform(post("/users/{userId}/points/add", userId)
                        .param("amount", String.valueOf(amount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Points added successfully"));

        verify(userPointService, times(1)).addPoints(userId, amount);
    }

    @Test
    void deductPoints_Success() throws Exception {
        // Given
        Long userId = 1L;
        int amount = 30;

        // When & Then
        mockMvc.perform(post("/users/{userId}/points/deduct", userId)
                        .param("amount", String.valueOf(amount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Points deducted successfully"));

        verify(userPointService, times(1)).deductPoints(userId, amount);
    }

    @Test
    void deductPoints_Fail_InsufficientPoints() throws Exception {
        // Given
        Long userId = 1L;
        int amount = 200;

        doThrow(new RuntimeException("Insufficient points")).when(userPointService).deductPoints(userId, amount);

        // When & Then
        mockMvc.perform(post("/users/{userId}/points/deduct", userId)
                        .param("amount", String.valueOf(amount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient points"));

        verify(userPointService, times(1)).deductPoints(userId, amount);
    }

    @Test
    void getPoints_Success() throws Exception {
        // Given
        Long userId = 1L;
        int expectedPoints = 100;

        when(userPointService.getPoints(userId)).thenReturn(expectedPoints);

        // When & Then
        mockMvc.perform(get("/users/{userId}/points", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));

        verify(userPointService, times(1)).getPoints(userId);
    }
}
