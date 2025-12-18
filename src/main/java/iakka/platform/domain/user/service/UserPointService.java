package iakka.platform.domain.user.service;

import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserPointService {
    private final UserRepository userRepository;

    public UserPointService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 포인트 추가
    public void addPoints(Long userId, int amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.addPoints(amount);
        userRepository.save(user);
    }

    // 사용자 포인트 차감 (잔액 부족 예외 처리)
    public void deductPoints(Long userId, int amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getPoints() < amount) {
            throw new RuntimeException("Insufficient points");
        }
        user.deductPoints(amount);
        userRepository.save(user);
    }

    // 사용자 포인트 조회
    public int getPoints(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getPoints();
    }
}
