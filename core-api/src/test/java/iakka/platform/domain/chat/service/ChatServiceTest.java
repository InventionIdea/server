package iakka.platform.domain.chat.service;

import iakka.platform.domain.chat.dto.ChatMessageDto;
import iakka.platform.domain.chat.entity.ChatMessage;
import iakka.platform.domain.chat.repository.ChatMessageRepository;
import iakka.platform.domain.crew.entity.Crew;
import iakka.platform.domain.crew.repository.CrewRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ChatServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private CrewRepository crewRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    void saveMessage_ShouldSaveAndReturnDto() {
        Long crewId = 1L;
        Long userId = 2L;
        String content = "Test message";

        Crew crew = new Crew(); crew.setId(crewId);
        User user = new User(); user.setId(userId);
        ChatMessageDto dto = new ChatMessageDto(userId, crewId, content);

        when(crewRepository.findById(crewId)).thenReturn(Optional.of(crew));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chatMessageRepository.save(any(ChatMessage.class))).thenAnswer(i -> i.getArgument(0));

        ChatMessageDto result = chatService.saveMessage(crewId, dto);

        assertThat(result.getContent()).isEqualTo(content);
        verify(chatMessageRepository, times(1)).save(any(ChatMessage.class));
    }
}
