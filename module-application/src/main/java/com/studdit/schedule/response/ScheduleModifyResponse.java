package com.studdit.schedule.response;

import com.studdit.schedule.Schedule;
import com.studdit.schedule.enums.Visibility;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleModifyResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private Visibility visibility;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @Builder
    private ScheduleModifyResponse(
            Long id,
            String title,
            String description,
            String category,
            Visibility visibility,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public static ScheduleModifyResponse of(Schedule schedule) {
        return ScheduleModifyResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .category(schedule.getCategory())
                .visibility(schedule.getVisibility())
                .startDateTime(schedule.getStartDateTime())
                .endDateTime(schedule.getEndDateTime())
                .build();
    }
}