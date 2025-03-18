package iakka.platform.domain.user.service;

import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // Spring 서비스 컴포넌트로 등록
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // UserRepository 의존성 주입 및 비밀번호 인코더 초기화
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // 사용자 등록 (비밀번호 암호화 후 저장)
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 비밀번호 암호화
        user.setPoints(0); // 초기 포인트 설정
        return userRepository.save(user); // 저장 후 반환
    }

    // 사용자 ID로 사용자 조회
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")); // 없으면 예외 발생
    }

    // 사용자 포인트 업데이트 (강제 설정)
    public void updatePoints(Long userId, int points) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPoints(points);
        userRepository.save(user);
    }

    // 사용자 포인트 추가
    public void addPoints(Long userId, int amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.addPoints(amount);
        userRepository.save(user);
    }

    // 사용자 포인트 차감 (포인트 부족 시 예외 발생)
    public void deductPoints(Long userId, int amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getPoints() < amount) {
            throw new RuntimeException("Insufficient points"); // 포인트 부족 예외 처리
        }
        user.deductPoints(amount);
        userRepository.save(user);
    }
}
