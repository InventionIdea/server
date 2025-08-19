package iakka.platform.domain.like.service;

import iakka.platform.domain.like.entity.Like;
import iakka.platform.domain.like.entity.Like.LikeType;
import iakka.platform.domain.like.repository.LikeRepository;
import iakka.platform.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    @Transactional
    public void like(LikeType type, Long targetId, User user) {
        likeRepository.findByTypeAndTargetIdAndUser(type, targetId, user)
                .orElseGet(() -> likeRepository.save(
                        Like.builder()
                                .type(type)
                                .targetId(targetId)
                                .user(user)
                                .build()
                ));
    }

    @Transactional
    public void unlike(LikeType type, Long targetId, User user) {
        likeRepository.deleteByTypeAndTargetIdAndUser(type, targetId, user);
    }

    public boolean isLiked(LikeType type, Long targetId, User user) {
        return likeRepository.findByTypeAndTargetIdAndUser(type, targetId, user).isPresent();
    }

    public long countLikes(LikeType type, Long targetId) {
        return likeRepository.countByTypeAndTargetId(type, targetId);
    }
}
