package iakka.platform.domain.like.entity;

import iakka.platform.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "likes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "type", "target_id"})})
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

    public enum LikeType {
        POST,
        IDEA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LikeType type;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
