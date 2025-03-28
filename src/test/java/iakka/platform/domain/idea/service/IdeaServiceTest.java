package iakka.platform.domain.idea.service;

import iakka.platform.domain.idea.entity.Idea;
import iakka.platform.domain.idea.repository.IdeaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IdeaServiceTest {

    private IdeaRepository ideaRepository;
    private WebClient webClient;
    private IdeaService ideaService;

    // WebClient 내부 체인 요소들
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    private WebClient.RequestBodySpec requestBodySpec;
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void 설정() {
        ideaRepository = mock(IdeaRepository.class);
        webClient = mock(WebClient.class);

        // WebClient 체인 mocking
        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        requestBodySpec = mock(WebClient.RequestBodySpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("video-url"));

        ideaService = new IdeaService(ideaRepository, webClient);
    }

    @Test
    void 아이디로_아이디어목록_조회() {
        String userId = "test-user";
        List<Idea> mockIdeas = Arrays.asList(new Idea(userId, "테스트 제목", null));
        when(ideaRepository.findByUserId(userId)).thenReturn(mockIdeas);

        List<Idea> result = ideaService.getIdeasByUserId(userId);

        assertEquals(1, result.size());
        assertEquals("테스트 제목", result.get(0).getTitle());
        verify(ideaRepository, times(1)).findByUserId(userId);
    }

    @Test
    void 비디오_생성() {
        String userId = "user1";
        String title = "타이틀";
        List<String> script = List.of("문장1", "문장2");

        Idea result = ideaService.generateVideo(userId, title, script).block();

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(title, result.getTitle());
        assertEquals("video-url", result.getFileId());
    }

    @Test
    void 파일ID_업데이트_성공() {
        String userId = "user1";
        String title = "아이디어 제목";
        String fileId = "file-999";

        Idea idea1 = new Idea(userId, title, null); // fileId가 null → 업데이트 대상
        Idea idea2 = new Idea(userId, title, "file-111"); // 이미 fileId 있음 → 무시

        List<Idea> ideas = Arrays.asList(idea1, idea2);
        when(ideaRepository.findByUserIdAndTitle(userId, title)).thenReturn(ideas);
        when(ideaRepository.save(any(Idea.class))).thenReturn(idea1);

        boolean result = ideaService.updateFileId(userId, title, fileId);

        assertTrue(result);
        assertEquals(fileId, idea1.getFileId());
        verify(ideaRepository, times(1)).save(idea1);
        verify(ideaRepository, never()).save(idea2); // 이미 fileId 있음
    }

    @Test
    void 파일ID_업데이트_실패() {
        String userId = "user1";
        String title = "아이디어 제목";
        String fileId = "file-999";

        // 모든 아이디어가 이미 fileId를 가지고 있음
        Idea idea1 = new Idea(userId, title, "file-123");
        Idea idea2 = new Idea(userId, title, "file-456");

        when(ideaRepository.findByUserIdAndTitle(userId, title))
                .thenReturn(List.of(idea1, idea2));

        boolean result = ideaService.updateFileId(userId, title, fileId);

        assertFalse(result);
        verify(ideaRepository, never()).save(any());
    }

    @Test
    void 아이디어_삭제_성공() {
        Long ideaId = 1L;
        Idea idea = new Idea("user1", "제목", "file-1");

        when(ideaRepository.findById(ideaId)).thenReturn(Optional.of(idea));

        boolean result = ideaService.deleteIdeaById(ideaId);

        assertTrue(result);
        verify(ideaRepository).deleteById(ideaId);
    }

    @Test
    void 아이디어_삭제_실패() {
        Long ideaId = 999L;

        when(ideaRepository.findById(ideaId)).thenReturn(Optional.empty());

        boolean result = ideaService.deleteIdeaById(ideaId);

        assertFalse(result);
        verify(ideaRepository, never()).deleteById(any());
    }
}
