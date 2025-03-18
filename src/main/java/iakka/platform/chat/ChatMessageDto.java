package iakka.platform.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessageDto {
    private Long senderId;
    private Long crewId;
    private String content;
}
