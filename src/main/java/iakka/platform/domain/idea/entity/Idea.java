package iakka.platform.domain.idea.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ideas")
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String title;

    @Column
    private String fileId;

    public Idea() {}

    public Idea(String userId, String title, String fileId) {
        this.userId = userId;
        this.title = title;
        this.fileId = fileId;
    }

    public Long getId() {
        return id;
    }

    //test용 setter 추가
    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
