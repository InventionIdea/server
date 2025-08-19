package iakka.platform.domain.idea.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class IdeaResponse {

    @JsonProperty("file_id")
    private String fileId;
}
