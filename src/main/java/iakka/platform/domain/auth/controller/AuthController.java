package iakka.platform.domain.auth.controller;

import iakka.platform.domain.auth.dto.LoginRequest;
import iakka.platform.domain.auth.dto.RegisterRequest;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import iakka.platform.jwt.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // 회원가입 (userId 추가)
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByUserId(request.getUserId()).isPresent()) {
            return ResponseEntity.badRequest().body("UserId already exists");
        }

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User newUser = new User();
        newUser.setUserId(request.getUserId()); // userId 저장
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // 비밀번호 암호화
        newUser.setRealName(request.getRealName());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setPoints(0); // 기본 포인트 설정

        userRepository.save(newUser);
        return ResponseEntity.ok("User registered successfully");
    }

    // 로그인 (userId 기반)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword()));

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUserId());
            String token = jwtUtil.generateToken(userDetails.getUsername()); // userId 기반으로 토큰 생성

            return ResponseEntity.ok(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
