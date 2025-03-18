package iakka.platform.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/chat/{crewId}")
    @SendTo("/topic/chat/{crewId}")
    public ChatMessageDto sendMessage(@DestinationVariable Long crewId, ChatMessageDto messageDto) {
        return chatService.saveMessage(crewId, messageDto);
    }
}
