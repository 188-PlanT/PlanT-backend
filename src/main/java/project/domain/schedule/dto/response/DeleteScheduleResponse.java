package project.domain.schedule.dto.response;

import lombok.Getter;

@Getter
public class DeleteScheduleResponse {
    private String message;

    public DeleteScheduleResponse() {
        this.message = "successfully delete schedule";
    }
}
