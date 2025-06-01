package project.domain.schedule.dto.response;

import lombok.Getter;

@Getter
public class RemoveChatResponse {
    private String message;

    public RemoveChatResponse() {
        this.message = "successfully delete schedule";
    }
}
