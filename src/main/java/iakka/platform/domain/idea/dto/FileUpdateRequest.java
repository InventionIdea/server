package iakka.platform.domain.idea.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileUpdateRequest {

    @JsonProperty("user_id")
    private String userId;

    private String title;

    @JsonProperty("file_id")
    private String fileId;
}
