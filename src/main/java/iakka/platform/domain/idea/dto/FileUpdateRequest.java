package iakka.platform.domain.idea.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileUpdateRequest {
    @JsonProperty("user_id")
    private String userId;

    private String title;

    @JsonProperty("file_id")
    private String fileId;

    public FileUpdateRequest() {}

    public FileUpdateRequest(String userId, String title, String fileId) {
        this.userId = userId;
        this.title = title;
        this.fileId = fileId;
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
