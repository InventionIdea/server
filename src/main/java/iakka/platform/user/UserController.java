package iakka.platform.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/{userId}/points/add")
    public ResponseEntity<String> addPoints(@PathVariable Long userId, @RequestParam int amount) {
        userService.addPoints(userId, amount);
        return ResponseEntity.ok("Points added successfully");
    }

    @PostMapping("/{userId}/points/deduct")
    public ResponseEntity<String> deductPoints(@PathVariable Long userId, @RequestParam int amount) {
        try {
            userService.deductPoints(userId, amount);
            return ResponseEntity.ok("Points deducted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/points")
    public ResponseEntity<Integer> getPoints(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user.getPoints());
    }
}