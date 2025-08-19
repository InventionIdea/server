package iakka.platform.domain.user.controller;

import iakka.platform.domain.user.dto.UserDto;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.service.UserService;
import iakka.platform.domain.user.service.UserPointService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserPointService userPointService;

    public UserController(UserService userService, UserPointService userPointService) {
        this.userService = userService;
        this.userPointService = userPointService;
    }

    // 사용자 포인트 추가
    @PostMapping("/{userId}/points/add")
    public ResponseEntity<String> addPoints(@PathVariable Long userId, @RequestParam int amount) {
        userPointService.addPoints(userId, amount);
        return ResponseEntity.ok("Points added successfully");
    }

    // 사용자 포인트 차감
    @PostMapping("/{userId}/points/deduct")
    public ResponseEntity<String> deductPoints(@PathVariable Long userId, @RequestParam int amount) {
        try {
            userPointService.deductPoints(userId, amount);
            return ResponseEntity.ok("Points deducted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 사용자 포인트 조회
    @GetMapping("/{userId}/points")
    public ResponseEntity<Integer> getPoints(@PathVariable Long userId) {
        return ResponseEntity.ok(userPointService.getPoints(userId));
    }

    // 사용자 계정 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId,
                                           @AuthenticationPrincipal UserDetails currentUser) {
        if (!userService.isCurrentUser(userId, currentUser)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}
