package iakka.platform.domain.chat.service;

import iakka.platform.domain.chat.dto.ChatMessageDto;
import iakka.platform.domain.chat.entity.ChatMessage;
import iakka.platform.domain.chat.request.ChatMessageRepository;
import iakka.platform.domain.crew.entity.Crew;
import iakka.platform.domain.crew.repository.CrewRepository;
import iakka.platform.domain.user.entity.User;
import iakka.platform.domain.user.repository.UserRepository;
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
