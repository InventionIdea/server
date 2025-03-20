package iakka.platform.domain.user.entity;

import iakka.platform.domain.post.entity.Post;
//import iakka.platform.domain.comment.entity.Comment;
import jakarta.persistence.*;
import java.util.List;

@Entity // JPA 엔티티로 선언
@Table(name = "users") // 데이터베이스 테이블명 지정
public class User {
    @Id // 기본 키(primary key) 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가(AUTO_INCREMENT) 설정
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // 사용자 비밀번호

    private int points; // 사용자 포인트

    private String realName;  // 본명
    private String phoneNumber; // 전화번호도 입력하도록 만들기!

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL) // 사용자가 작성한 게시글 (1:N 관계)
    private List<Post> posts;
//
//    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL) // 사용자가 작성한 댓글 (1:N 관계)
//    private List<Comment> comments;

    // Getter 및 Setter 메서드

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public List<Post> getPosts() { return posts; }
    public void setPosts(List<Post> posts) { this.posts = posts; }

//    public List<Comment> getComments() { return comments; }
//    public void setComments(List<Comment> comments) { this.comments = comments; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }


    // 포인트 추가 메서드
    public void addPoints(int amount) {
        this.points += amount;
    }

    // 포인트 차감 메서드
    public void deductPoints(int amount) {
        this.points -= amount;
    }
}
