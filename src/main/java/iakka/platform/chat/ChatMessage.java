package iakka.platform.chat;

import iakka.platform.user.User;
import iakka.platform.crew.Crew;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ChatMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "crew_id")
    private Crew crew;

    private LocalDateTime createdAt = LocalDateTime.now();
}
