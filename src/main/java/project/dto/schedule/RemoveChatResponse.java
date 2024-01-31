package project.dto.schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class RemoveChatResponse{
    private String message;
    
    public RemoveChatResponse(){
        this.message = "successfully delete schedule";
    }
}