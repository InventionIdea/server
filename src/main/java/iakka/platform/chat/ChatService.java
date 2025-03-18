package iakka.platform.chat;

import iakka.platform.crew.Crew;
import iakka.platform.crew.CrewRepository;
import iakka.platform.user.User;
import iakka.platform.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final CrewRepository crewRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatMessageDto saveMessage(Long crewId, ChatMessageDto messageDto) {
        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new RuntimeException("Crew not found"));
        User user = userRepository.findById(messageDto.getSenderId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setCrew(crew);
        chatMessage.setSender(user);
        chatMessage.setContent(messageDto.getContent());

        chatMessageRepository.save(chatMessage);

        return new ChatMessageDto(user.getId(), crew.getId(), chatMessage.getContent());
    }
}
