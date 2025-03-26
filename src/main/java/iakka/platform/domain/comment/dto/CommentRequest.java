package iakka.platform.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import iakka.platform.domain.comment.entity.Comment.CommentType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentRequest {
    @JsonProperty("type")
    private CommentType type;

    @JsonProperty("target_id")
    private Long targetId;

    @JsonProperty("author_id")
    private Long authorId;

    @JsonProperty("parent_comment_id")
    private Long parentCommentId;

    @JsonProperty("content")
    private String content;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
