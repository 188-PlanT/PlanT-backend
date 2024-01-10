package project.dto.devLog;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDevLogRequest{
    @NotNull
    private Long scheduleId;
    
    @NotBlank
    private Long userId;
    
    @NotBlank
    private String content;
}