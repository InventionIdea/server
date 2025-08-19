package iakka.platform.domain.user.entity;

import iakka.platform.domain.post.entity.Post;
// import iakka.platform.domain.comment.entity.Comment;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private int points;

    private String realName;
    private String phoneNumber;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;

    // @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    // private List<Comment> comments;

    // 포인트 추가 메서드
    public void addPoints(int amount) {
        this.points += amount;
    }

    // 포인트 차감 메서드
    public void deductPoints(int amount) {
        this.points -= amount;
    }
}
