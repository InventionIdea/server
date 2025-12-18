package iakka.platform.domain.crew.service;

import iakka.platform.domain.crew.dto.CrewRequest;
import iakka.platform.domain.crew.entity.Crew;
import iakka.platform.domain.crew.entity.CrewMember;
import iakka.platform.domain.crew.repository.CrewMemberRepository;
import iakka.platform.domain.crew.repository.CrewRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

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

        User user = new User();
        user.setId(1L);

        when(crewRepository.save(any(Crew.class))).thenReturn(saved);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Crew result = crewService.createCrew(request, 1L);

        assertEquals("테스트 크루", result.getName());
        assertEquals("테스트 설명", result.getDescription());
        verify(crewMemberRepository).save(any(CrewMember.class));
    }

    @Test
    void 크루_수정() {
        Crew crew = new Crew();
        crew.setId(1L);
        crew.setName("Old Name");
        crew.setDescription("Old Desc");

        CrewRequest request = new CrewRequest();
        request.setName("New Name");
        request.setDescription("New Desc");

        when(crewRepository.findById(1L)).thenReturn(Optional.of(crew));
        when(crewRepository.save(any(Crew.class))).thenReturn(crew);

        Crew updated = crewService.updateCrew(1L, request);

        assertEquals("New Name", updated.getName());
        assertEquals("New Desc", updated.getDescription());
        verify(crewRepository, times(1)).save(crew);
    }

    @Test
    void 크루_가입_정상_동작() {
        User user = new User();
        user.setId(2L);

        Crew crew = new Crew();
        crew.setId(1L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(crewRepository.findById(1L)).thenReturn(Optional.of(crew));
        when(crewMemberRepository.countByUser(user)).thenReturn(1);

        crewService.joinCrew(2L, 1L);

        verify(crewMemberRepository).save(any(CrewMember.class));
    }

    @Test
    void 크루_가입_최대_제한_예외() {
        User user = new User();
        user.setId(2L);

        Crew crew = new Crew();
        crew.setId(1L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(crewRepository.findById(1L)).thenReturn(Optional.of(crew));
        when(crewMemberRepository.countByUser(user)).thenReturn(2); // 이미 2개 가입됨

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                crewService.joinCrew(2L, 1L));

        assertEquals("User can join up to 2 crews only.", ex.getMessage());
    }

    @Test
    void 크루_탈퇴_정상_동작() {
        User user = new User();
        user.setId(2L);

        Crew crew = new Crew();
        crew.setId(1L);

        CrewMember member = new CrewMember();
        member.setCrew(crew);
        member.setUser(user);

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(crewRepository.findById(1L)).thenReturn(Optional.of(crew));
        when(crewMemberRepository.findByUserAndCrew(user, crew)).thenReturn(Optional.of(member));

        crewService.leaveCrew(2L, 1L);

        verify(crewMemberRepository).delete(member);
    }

    @Test
    void 크루_탈퇴_비가입_예외() {
        User user = new User();
        Crew crew = new Crew();

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(crewRepository.findById(1L)).thenReturn(Optional.of(crew));
        when(crewMemberRepository.findByUserAndCrew(user, crew)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                crewService.leaveCrew(2L, 1L));

        assertEquals("User is not a member of this crew", ex.getMessage());
    }

    @Test
    void 마지막_멤버_탈퇴시_크루_삭제된다() {
        User user = new User();
        user.setId(2L);

        Crew crew = new Crew();
        crew.setId(1L);

        CrewMember member = new CrewMember();
        member.setUser(user);
        member.setCrew(crew);

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(crewRepository.findById(1L)).thenReturn(Optional.of(crew));
        when(crewMemberRepository.findByUserAndCrew(user, crew)).thenReturn(Optional.of(member));
        when(crewMemberRepository.countByCrew(crew)).thenReturn(0L); // 마지막 멤버

        crewService.leaveCrew(2L, 1L);

        verify(crewMemberRepository).delete(member);
        verify(crewRepository).delete(crew);
    }
}
