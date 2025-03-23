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
}
