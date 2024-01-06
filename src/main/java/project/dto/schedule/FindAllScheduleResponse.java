package project.dto.schedule;

import lombok.Getter;
import lombok.AllArgsConstructor;
import java.util.List;

@Getter
@AllArgsConstructor
public class FindAllScheduleResponse{
    private int totalPages;
    private int pageNumber;
    private List<ScheduleDto> data;
}