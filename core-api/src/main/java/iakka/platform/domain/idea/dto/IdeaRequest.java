package iakka.platform.domain.idea.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdeaRequest {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("script")
    private List<String> script;
}
