package iakka.platform.domain.user.service;

import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPointServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserPointService userPointService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUserId("testUserId");
        testUser.setPoints(100); // 초기 포인트 설정
    }

    @Test
    void addPoints_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        userPointService.addPoints(1L, 50);

        // Then
        assertEquals(150, testUser.getPoints());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void deductPoints_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        userPointService.deductPoints(1L, 50);

        // Then
        assertEquals(50, testUser.getPoints());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void deductPoints_Fail_InsufficientPoints() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When & Then (잔액 부족 예외 발생 확인)
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userPointService.deductPoints(1L, 150));
        assertEquals("Insufficient points", exception.getMessage());

        // 포인트가 차감되지 않았는지 확인
        assertEquals(100, testUser.getPoints());
        verify(userRepository, never()).save(testUser);
    }

    @Test
    void getPoints_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        int points = userPointService.getPoints(1L);

        // Then
        assertEquals(100, points);
    }

    @Test
    void getPoints_Fail_UserNotFound() {
        // Given
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // When & Then (사용자를 찾을 수 없는 경우 예외 발생 확인)
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userPointService.getPoints(2L));
        assertEquals("User not found", exception.getMessage());
    }
}
