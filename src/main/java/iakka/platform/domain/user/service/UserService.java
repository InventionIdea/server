package iakka.platform.domain.user.service;

import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 사용자 등록 (비밀번호 암호화 후 저장)
    public User register(User user) {
        if (isUserIdExists(user.getUserId())) {
            throw new RuntimeException("UserId already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPoints(0);
        return userRepository.save(user);
    }

    // userId 기반으로 사용자 조회
    public User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with userId: " + userId));
    }

    // userId 중복 확인
    public boolean isUserIdExists(String userId) {
        return userRepository.findByUserId(userId).isPresent();
    }
}
