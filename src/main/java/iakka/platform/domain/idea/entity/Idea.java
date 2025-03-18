package iakka.platform.domain.idea.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    @Column(nullable = false, length = 1000)
    private String script;

    @Column(nullable = true)
    private String fileId;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Idea() {}

    public Idea(String userId, String title, String script, String fileId) {
        this.userId = userId;
        this.title = title;
        this.script = script;
        this.fileId = fileId;
    }

    public Long getId() {
        return id;
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

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}