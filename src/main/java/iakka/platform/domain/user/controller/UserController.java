package iakka.platform.domain.user.controller;

import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // RESTful 컨트롤러로 지정
@RequestMapping("/users") // 기본 경로 설정
public class UserController {
    private final UserService userService;

    // UserService 의존성 주입
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 사용자 등록 API
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    // 사용자 포인트 추가 API
    @PostMapping("/{userId}/points/add")
    public ResponseEntity<String> addPoints(@PathVariable Long userId, @RequestParam int amount) {
        userService.addPoints(userId, amount);
        return ResponseEntity.ok("Points added successfully");
    }

    // 사용자 포인트 차감 API (예외 처리 포함)
    @PostMapping("/{userId}/points/deduct")
    public ResponseEntity<String> deductPoints(@PathVariable Long userId, @RequestParam int amount) {
        try {
            userService.deductPoints(userId, amount);
            return ResponseEntity.ok("Points deducted successfully");
        } catch (RuntimeException e) { // 예외 발생 시 클라이언트에 오류 메시지 반환
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 사용자 포인트 조회 API
    @GetMapping("/{userId}/points")
    public ResponseEntity<Integer> getPoints(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user.getPoints());
    }
}
