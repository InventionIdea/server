package iakka.platform.domain.user.service;

import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    //ID기반으로 사용자 삭제
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }

    // 현재 사용자와 삭제하고자 하는 사용자가 같은지 확인
    public boolean isCurrentUser(Long userId, UserDetails currentUser) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(u -> u.getUserId().equals(currentUser.getUsername())).orElse(false);
    }
}
