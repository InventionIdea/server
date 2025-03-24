package iakka.platform.domain.post.entity;

// import iakka.platform.domain.comment.entity.Comment;
import iakka.platform.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    // private List<Comment> comments;

}
