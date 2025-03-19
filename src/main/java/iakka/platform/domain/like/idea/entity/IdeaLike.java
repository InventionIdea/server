package iakka.platform.domain.like.idea.entity;

import iakka.platform.domain.idea.entity.Idea;
import iakka.platform.domain.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "idea_likes", uniqueConstraints = {@UniqueConstraint(columnNames = {"idea_id", "user_id"})})
public class IdeaLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idea_id", nullable = false)
    private Idea idea;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public IdeaLike() {}

    public IdeaLike(Idea idea, User user) {
        this.idea = idea;
        this.user = user;
    }

    public Long getId() { return id; }
    public Idea getIdea() { return idea; }
    public User getUser() { return user; }
}
