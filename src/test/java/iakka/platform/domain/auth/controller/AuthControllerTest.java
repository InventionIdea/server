package iakka.platform.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import iakka.platform.domain.auth.dto.LoginRequest;
import iakka.platform.domain.auth.dto.RegisterRequest;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import iakka.platform.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthController authController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void register_Success() throws Exception {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setUserId("testUserId");
        request.setUsername("testUsername");
        request.setPassword("testPassword");
        request.setRealName("John Doe");
        request.setPhoneNumber("010-1234-5678");

        when(userRepository.findByUserId(request.getUserId())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setUserId(request.getUserId());
        savedUser.setUsername(request.getUsername());
        savedUser.setPassword("encodedPassword");
        savedUser.setRealName(request.getRealName());
        savedUser.setPhoneNumber(request.getPhoneNumber());
        savedUser.setPoints(0);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When & Then (회원가입 API 요청)
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_Fail_WhenUserIdExists() throws Exception {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setUserId("testUserId");
        request.setUsername("testUsername");
        request.setPassword("testPassword");

        when(userRepository.findByUserId(request.getUserId())).thenReturn(Optional.of(new User()));

        // When & Then (회원가입 실패: userId 중복)
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("UserId already exists"));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() throws Exception {
        // Given
        LoginRequest request = new LoginRequest();
        request.setUserId("testUserId");
        request.setPassword("testPassword");

        UserDetails userDetails = Mockito.mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUserId");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userDetailsService.loadUserByUsername(request.getUserId())).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails.getUsername())).thenReturn("mockJwtToken");

        // When & Then (로그인 API 요청)
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("mockJwtToken"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_Fail_InvalidCredentials() throws Exception {
        // Given
        LoginRequest request = new LoginRequest();
        request.setUserId("testUserId");
        request.setPassword("wrongPassword");

        doThrow(new BadCredentialsException("Invalid credentials")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // When & Then (로그인 실패: 잘못된 자격증명)
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized()) // 401 응답을 기대
                .andExpect(content().string("Invalid credentials"));
    }
}
