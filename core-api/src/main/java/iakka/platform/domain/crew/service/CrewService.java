package iakka.platform.domain.crew.service;

import iakka.platform.domain.crew.dto.CrewRequest;
import iakka.platform.domain.crew.entity.Crew;
import iakka.platform.domain.crew.entity.CrewMember;
import iakka.platform.domain.crew.repository.CrewMemberRepository;
import iakka.platform.domain.crew.repository.CrewRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrewService {
    private final CrewRepository crewRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final UserRepository userRepository;

    @Transactional
    public Crew createCrew(CrewRequest request, Long userId) {
        Crew crew = new Crew();
        crew.setName(request.getName());
        crew.setDescription(request.getDescription());
        Crew savedCrew = crewRepository.save(crew);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CrewMember member = new CrewMember();
        member.setUser(user);
        member.setCrew(savedCrew);
        crewMemberRepository.save(member);

        return savedCrew;
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

    @Transactional
    public void leaveCrew(Long userId, Long crewId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("Crew not found"));

        CrewMember crewMember = crewMemberRepository.findByUserAndCrew(user, crew)
                .orElseThrow(() -> new RuntimeException("User is not a member of this crew"));

        crewMemberRepository.delete(crewMember);

        long remainingMembers = crewMemberRepository.countByCrew(crew);
        if (remainingMembers == 0) {
            crewRepository.delete(crew);
        }
    }

    public Crew updateCrew(Long crewId, CrewRequest request) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("Crew not found"));
        crew.setName(request.getName());
        crew.setDescription(request.getDescription());
        return crewRepository.save(crew);
    }
}