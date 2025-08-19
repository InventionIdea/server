package iakka.platform.domain.user.repository;

import iakka.platform.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void userId로_사용자를_조회한다() {
        User user = new User();
        user.setUserId("uniqueId");
        user.setUsername("testUser");
        user.setPassword("pw");
        user.setRealName("홍길동");
        user.setPhoneNumber("01012345678");

        userRepository.save(user);

        Optional<User> found = userRepository.findByUserId("uniqueId");

        assertTrue(found.isPresent());
        assertEquals("testUser", found.get().getUsername());
    }
}
