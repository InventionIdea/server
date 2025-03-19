package iakka.platform.domain.comment.idea.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdeaCommentRequest {
    @JsonProperty("idea_id")
    private Long ideaId;

    @JsonProperty("author_id")
    private Long authorId;

    @JsonProperty("parent_comment_id")
    private Long parentCommentId;

    @JsonProperty("content")
    private String content;

    public Long getIdeaId() { return ideaId; }
    public void setIdeaId(Long ideaId) { this.ideaId = ideaId; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public Long getParentCommentId() { return parentCommentId; }
    public void setParentCommentId(Long parentCommentId) { this.parentCommentId = parentCommentId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
