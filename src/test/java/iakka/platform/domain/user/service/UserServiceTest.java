package iakka.platform.domain.user.service;

import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId("testUserId");
        testUser.setUsername("testUsername");
        testUser.setPassword("plainPassword");
        testUser.setRealName("John Doe");
        testUser.setPhoneNumber("010-1234-5678");
        testUser.setPoints(0);
    }

    @Test
    void register_Success() {
        // Given
        when(userRepository.findByUserId(testUser.getUserId())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        User registeredUser = userService.register(testUser);

        // Then
        assertNotNull(registeredUser);
        assertEquals("testUserId", registeredUser.getUserId());
        assertEquals("encodedPassword", registeredUser.getPassword()); // 암호화된 패스워드 확인
        assertEquals(0, registeredUser.getPoints()); // 기본 포인트 확인

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_Fail_WhenUserIdExists() {
        // Given
        when(userRepository.findByUserId(testUser.getUserId())).thenReturn(Optional.of(testUser));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.register(testUser));
        assertEquals("UserId already exists", exception.getMessage());

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserByUserId_Success() {
        // Given
        when(userRepository.findByUserId(testUser.getUserId())).thenReturn(Optional.of(testUser));

        // When
        User foundUser = userService.getUserByUserId("testUserId");

        // Then
        assertNotNull(foundUser);
        assertEquals("testUserId", foundUser.getUserId());
        assertEquals("testUsername", foundUser.getUsername());

        verify(userRepository, times(1)).findByUserId("testUserId");
    }

    @Test
    void getUserByUserId_Fail_WhenUserNotFound() {
        // Given
        when(userRepository.findByUserId("nonExistentUserId")).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserByUserId("nonExistentUserId"));
        assertEquals("User not found with userId: nonExistentUserId", exception.getMessage());

        verify(userRepository, times(1)).findByUserId("nonExistentUserId");
    }

    @Test
    void isUserIdExists_True() {
        // Given
        when(userRepository.findByUserId(testUser.getUserId())).thenReturn(Optional.of(testUser));

        // When
        boolean exists = userService.isUserIdExists("testUserId");

        // Then
        assertTrue(exists);
        verify(userRepository, times(1)).findByUserId("testUserId");
    }

    @Test
    void isUserIdExists_False() {
        // Given
        when(userRepository.findByUserId("nonExistentUserId")).thenReturn(Optional.empty());

        // When
        boolean exists = userService.isUserIdExists("nonExistentUserId");

        // Then
        assertFalse(exists);
        verify(userRepository, times(1)).findByUserId("nonExistentUserId");
    }
}
