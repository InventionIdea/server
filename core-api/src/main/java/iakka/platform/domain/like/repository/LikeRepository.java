package iakka.platform.domain.like.repository;

import iakka.platform.domain.like.entity.Like;
import iakka.platform.domain.like.entity.Like.LikeType;
import iakka.platform.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByTypeAndTargetIdAndUser(LikeType type, Long targetId, User user);
    void deleteByTypeAndTargetIdAndUser(LikeType type, Long targetId, User user);
    // 특정 대상의 좋아요 수 카운트
    Long countByTypeAndTargetId(LikeType type, Long targetId);
}
