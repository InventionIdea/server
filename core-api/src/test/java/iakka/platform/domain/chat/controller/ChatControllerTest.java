package iakka.platform.domain.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import iakka.platform.domain.chat.dto.ChatMessageDto;
import iakka.platform.domain.chat.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.handler.annotation.DestinationVariable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ChatControllerTest {

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    void sendMessage_ShouldCallServiceAndReturnDto() {
        Long crewId = 1L;
        ChatMessageDto input = new ChatMessageDto(2L, crewId, "Hi there");
        ChatMessageDto expected = new ChatMessageDto(2L, crewId, "Hi there");

        when(chatService.saveMessage(crewId, input)).thenReturn(expected);

        ChatMessageDto result = chatController.sendMessage(crewId, input);

        assertThat(result.getContent()).isEqualTo("Hi there");
        verify(chatService).saveMessage(crewId, input);
    }
}
