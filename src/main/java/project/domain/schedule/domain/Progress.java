package project.domain.schedule.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Progress {
    
    TODO("TO_DO", "실행전"),
    INPROGRESS("IN_PROGRESS", "실행중"),
    DONE("DONE", "완료");
    
    private final String key;
    private final String title;
}