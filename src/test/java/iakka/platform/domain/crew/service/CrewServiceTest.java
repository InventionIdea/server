package iakka.platform.domain.crew.service;

import iakka.platform.domain.crew.dto.CrewRequest;
import iakka.platform.domain.crew.entity.Crew;
import iakka.platform.domain.crew.repository.CrewMemberRepository;
import iakka.platform.domain.crew.repository.CrewRepository;
import iakka.platform.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CrewServiceTest {

    private CrewRepository crewRepository;
    private CrewMemberRepository crewMemberRepository;
    private UserRepository userRepository;
    private CrewService crewService;

    @BeforeEach
    void 설정() {
        crewRepository = mock(CrewRepository.class);
        crewMemberRepository = mock(CrewMemberRepository.class);
        userRepository = mock(UserRepository.class);
        crewService = new CrewService(crewRepository, crewMemberRepository, userRepository);
    }

    @Test
    void 크루_생성() {
        CrewRequest request = new CrewRequest();
        request.setName("테스트 크루");
        request.setDescription("테스트 설명");

        Crew saved = new Crew();
        saved.setName("테스트 크루");
        saved.setDescription("테스트 설명");

        when(crewRepository.save(any(Crew.class))).thenReturn(saved);

        Crew result = crewService.createCrew(request);

        assertEquals("테스트 크루", result.getName());
        assertEquals("테스트 설명", result.getDescription());
        verify(crewRepository, times(1)).save(any(Crew.class));
    }
}
