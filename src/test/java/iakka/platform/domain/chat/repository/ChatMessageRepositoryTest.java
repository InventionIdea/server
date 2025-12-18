package iakka.platform.domain.chat.repository;

import iakka.platform.domain.chat.entity.ChatMessage;
import iakka.platform.domain.crew.entity.Crew;
import iakka.platform.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChatMessageRepositoryTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("채팅 메시지를 저장하고 조회할 수 있다.")
    void saveAndFindChatMessage() {
        // 1. 유저 저장
        User sender = new User();
        sender.setUsername("tester");
        sender.setPassword("1234");         // <- Not Null
        sender.setUserId("tester01");       // <- Not Null (Unique)
        sender.setRealName("테스터");
        sender.setPoints(0);                // <- Not Null
        entityManager.persist(sender);

        // 2. 크루 저장
        Crew crew = new Crew();
        crew.setName("테스트크루");
        crew.setDescription("설명");
        entityManager.persist(crew);

        // 3. 메시지 저장
        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setCrew(crew);
        message.setContent("안녕하세요!");

        chatMessageRepository.save(message);

        // 4. 조회 및 검증
        List<ChatMessage> messages = chatMessageRepository.findAll();
        assertThat(messages).hasSize(1);
        ChatMessage saved = messages.get(0);

        assertThat(saved.getContent()).isEqualTo("안녕하세요!");
        assertThat(saved.getSender().getUsername()).isEqualTo("tester");
        assertThat(saved.getCrew().getName()).isEqualTo("테스트크루");
    }
}
