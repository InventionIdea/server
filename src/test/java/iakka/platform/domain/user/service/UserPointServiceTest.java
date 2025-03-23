package iakka.platform.domain.user.service;

import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserPointServiceTest {

    private UserRepository userRepository;
    private UserPointService pointService;

    @BeforeEach
    public void 설정() {
        userRepository = mock(UserRepository.class);
        pointService = new UserPointService(userRepository);
    }

    @Test
    public void 포인트를_추가할_수_있다() {
        User user = new User();
        user.setId(1L);
        user.setPoints(100);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        pointService.addPoints(1L, 50);

        assertEquals(150, user.getPoints());
        verify(userRepository).save(user);
    }

    @Test
    public void 포인트를_차감할_수_있다() {
        User user = new User();
        user.setId(1L);
        user.setPoints(100);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        pointService.deductPoints(1L, 40);

        assertEquals(60, user.getPoints());
        verify(userRepository).save(user);
    }

    @Test
    public void 포인트가_부족하면_예외를_던진다() {
        User user = new User();
        user.setId(1L);
        user.setPoints(10);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            pointService.deductPoints(1L, 50);
        });

        assertEquals("Insufficient points", ex.getMessage());
    }

    @Test
    public void 포인트를_조회할_수_있다() {
        User user = new User();
        user.setId(1L);
        user.setPoints(500);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        int result = pointService.getPoints(1L);
        assertEquals(500, result);
    }
}
