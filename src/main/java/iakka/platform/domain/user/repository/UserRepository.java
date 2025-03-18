package iakka.platform.domain.user.repository;

import iakka.platform.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// User 엔티티를 관리하는 JPA 리포지토리 인터페이스
public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자명을 기준으로 사용자를 찾는 메서드 (Optional<User> 반환)
    Optional<User> findByUsername(String username);
}