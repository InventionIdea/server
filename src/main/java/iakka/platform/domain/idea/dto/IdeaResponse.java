package iakka.platform.domain.idea.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IdeaResponse {
    @JsonProperty("file_id")
    private String fileId;

    public String getFileId() {
        return fileId;
    }
}
