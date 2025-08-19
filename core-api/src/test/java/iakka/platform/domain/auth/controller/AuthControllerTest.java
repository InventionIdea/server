package iakka.platform.domain.auth.controller;

import iakka.platform.domain.auth.dto.LoginRequest;
import iakka.platform.domain.auth.dto.RegisterRequest;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import iakka.platform.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;
    private AuthController authController;

    @BeforeEach
    public void 설정() {
        authenticationManager = mock(AuthenticationManager.class);
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtil = mock(JwtUtil.class);
        userDetailsService = mock(UserDetailsService.class);

        authController = new AuthController(authenticationManager, userRepository, passwordEncoder, jwtUtil, userDetailsService);
    }

    @Test
    public void 회원가입_성공한다() {
        RegisterRequest request = new RegisterRequest();
        request.setUserId("uid123");
        request.setUsername("user1");
        request.setPassword("pw");
        request.setRealName("홍길동");
        request.setPhoneNumber("01012345678");

        when(userRepository.findByUserId("uid123")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("user1")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pw")).thenReturn("encodedPw");

        ResponseEntity<String> response = authController.register(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully", response.getBody());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void 로그인_성공하면_JWT_토큰을_반환한다() {
        LoginRequest request = new LoginRequest();
        request.setUserId("uid123");
        request.setPassword("pw");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("uid123");
        when(userDetailsService.loadUserByUsername("uid123")).thenReturn(userDetails);
        when(jwtUtil.generateToken("uid123")).thenReturn("jwt.token");

        ResponseEntity<String> response = authController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("jwt.token", response.getBody());
    }
}
