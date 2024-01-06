package project.dto.devLog;

import lombok.Getter;
import lombok.AllArgsConstructor;

import java.util.List; 

@Getter
@AllArgsConstructor
public class DevLogFindAllResponse{
    private int totalPages;
    private int pageNumber;
    private List<DevLogDto> data;
}