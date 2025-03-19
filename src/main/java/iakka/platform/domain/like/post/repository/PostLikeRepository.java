package iakka.platform.domain.like.post.repository;

import iakka.platform.domain.like.post.entity.PostLike;
import iakka.platform.domain.post.entity.Post;
import iakka.platform.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndUser(Post post, User user);
    long countByPost(Post post);
    void deleteByPostAndUser(Post post, User user);
}
