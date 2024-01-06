package project.dto.schedule;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateScheduleRequest{
    @NotBlank
    private String workspace;
    
    @NotBlank
    private String name;
    
    private List<String> users;
}