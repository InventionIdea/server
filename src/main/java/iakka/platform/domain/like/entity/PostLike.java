package iakka.platform.domain.like.entity;

import iakka.platform.domain.post.entity.Post;
import iakka.platform.domain.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "post_likes", uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "user_id"})})
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public PostLike() {}

    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    public Long getId() { return id; }
    public Post getPost() { return post; }
    public User getUser() { return user; }
}
