package iakka.platform.domain.idea.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IdeaRequest {
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("script")
    private List<String> script;

    public IdeaRequest() {}

    public IdeaRequest(String userId, String title, List<String> script) {
        this.userId = userId;
        this.title = title;
        this.script = script;
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

    public List<String> getScript() {
        return script;
    }

    public void setScript(List<String> script) {
        this.script = script;
    }
}
