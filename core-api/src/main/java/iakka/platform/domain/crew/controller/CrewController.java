package iakka.platform.domain.crew.controller;

import iakka.platform.domain.crew.dto.CrewRequest;
import iakka.platform.domain.crew.service.CrewService;
import iakka.platform.domain.crew.entity.Crew;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crews")
@RequiredArgsConstructor
public class CrewController {
    private final CrewService crewService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Crew> createCrew(@RequestBody CrewRequest request,
                                           @AuthenticationPrincipal UserDetails currentUser) {
        User user = userRepository.findByUserId(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Crew crew = crewService.createCrew(request, user.getId());
        return ResponseEntity.ok(crew);
    }

    // 크루에 합류하는 API
    @PostMapping("/{crewId}/join/{userId}")
    public ResponseEntity<String> joinCrew(@PathVariable Long crewId, @PathVariable Long userId) {
        crewService.joinCrew(userId, crewId);
        return ResponseEntity.ok("Joined the crew successfully!");
    }

    // 크루를 탈퇴하는 API
    @PostMapping("/{crewId}/leave/{userId}")
    public ResponseEntity<String> leaveCrew(@PathVariable Long crewId, @PathVariable Long userId) {
        crewService.leaveCrew(userId, crewId);
        return ResponseEntity.ok("Left the crew successfully!");
    }

    @PutMapping("/{crewId}")
    public ResponseEntity<Crew> updateCrew(@PathVariable Long crewId, @RequestBody CrewRequest request) {
        Crew crew = crewService.updateCrew(crewId, request);
        return ResponseEntity.ok(crew);
    }
}
