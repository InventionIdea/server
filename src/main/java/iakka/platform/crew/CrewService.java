package iakka.platform.crew;

import iakka.platform.user.User;
import iakka.platform.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrewService {
    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final UserRepository userRepository;

    public Crew createCrew(CrewRequest request) {
        Crew crew = new Crew();
        crew.setName(request.getName());
        crew.setDescription(request.getDescription());
        return crewRepository.save(crew);
    }

    @Transactional
    public void joinCrew(Long userId, Long crewId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("Crew not found"));

        if (crewMemberRepository.countByUser(user) >= 2) {
            throw new RuntimeException("User can join up to 2 crews only.");
        }

        CrewMember member = new CrewMember();
        member.setUser(user);
        member.setCrew(crew);
        crewMemberRepository.save(member);
    }
}
