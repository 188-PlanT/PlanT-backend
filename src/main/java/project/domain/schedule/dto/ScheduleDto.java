package project.domain.schedule.dto;

import project.domain.schedule.domain.Schedule;

public record ScheduleDto(Long scheduleId, String name, String content) {

    public static ScheduleDto of(Schedule schedule) {
        return new ScheduleDto(schedule.getId(), schedule.getName(), schedule.getContent());
    }
}
