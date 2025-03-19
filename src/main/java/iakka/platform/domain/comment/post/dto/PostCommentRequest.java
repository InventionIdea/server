package iakka.platform.domain.comment.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostCommentRequest {
    @JsonProperty("post_id")
    private Long postId;

    @JsonProperty("author_id")
    private Long authorId;

    @JsonProperty("parent_comment_id")
    private Long parentCommentId;

    @JsonProperty("content")
    private String content;

    public Long getPostId() { return postId; }
    public void setPostId(Long postId) { this.postId = postId; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public Long getParentCommentId() { return parentCommentId; }
    public void setParentCommentId(Long parentCommentId) { this.parentCommentId = parentCommentId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
