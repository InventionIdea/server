package iakka.platform.domain.user.service;

import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    public void 설정() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    public void 회원가입_정상_처리된다() {
        User user = new User();
        user.setUserId("user1");
        user.setPassword("pw");

        when(userRepository.findByUserId("user1")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pw")).thenReturn("encodedPw");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.register(user);

        assertEquals("encodedPw", saved.getPassword());
        verify(userRepository).save(saved);
    }

    @Test
    public void 아이디로_유저를_조회한다() {
        User user = new User();
        user.setUserId("user2");

        when(userRepository.findByUserId("user2")).thenReturn(Optional.of(user));

        User found = userService.getUserByUserId("user2");

        assertEquals("user2", found.getUserId());
    }
}
