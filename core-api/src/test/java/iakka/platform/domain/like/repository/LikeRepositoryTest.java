package iakka.platform.domain.like.repository;

import iakka.platform.domain.like.entity.Like;
import iakka.platform.domain.like.entity.Like.LikeType;
import iakka.platform.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;

    @Test
    @DisplayName("Like 저장 및 조회 테스트")
    void saveAndFindByTypeAndTargetIdAndUser() {
        User user = new User(); user.setId(1L); // 가짜 유저 ID 설정
        Like like = Like.builder()
                .type(LikeType.POST)
                .targetId(100L)
                .user(user)
                .build();

        likeRepository.save(like);

        var result = likeRepository.findByTypeAndTargetIdAndUser(LikeType.POST, 100L, user);
        assertThat(result).isPresent();
        assertThat(result.get().getTargetId()).isEqualTo(100L);
    }
}
