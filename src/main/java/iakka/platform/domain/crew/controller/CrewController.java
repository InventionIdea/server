package iakka.platform.domain.crew.controller;

import iakka.platform.domain.crew.dto.CrewRequest;
import iakka.platform.domain.crew.service.CrewService;
import iakka.platform.domain.crew.entity.Crew;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crews")
@RequiredArgsConstructor
public class CrewController {
    private final CrewService crewService;

    @PostMapping
    public ResponseEntity<Crew> createCrew(@RequestBody CrewRequest request) {
        Crew crew = crewService.createCrew(request);
        return ResponseEntity.ok(crew);
    }

    @PostMapping("/{crewId}/join/{userId}")
    public ResponseEntity<String> joinCrew(@PathVariable Long crewId, @PathVariable Long userId) {
        crewService.joinCrew(userId, crewId);
        return ResponseEntity.ok("Joined the crew successfully!");
    }
}
