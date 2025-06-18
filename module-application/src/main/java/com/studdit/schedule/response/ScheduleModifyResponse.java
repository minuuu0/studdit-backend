package com.studdit.schedule.response;

import com.studdit.schedule.SingleSchedule;
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

    public static ScheduleModifyResponse of(SingleSchedule singleSchedule) {
        return ScheduleModifyResponse.builder()
                .id(singleSchedule.getId())
                .title(singleSchedule.getTitle())
                .description(singleSchedule.getDescription())
                .category(singleSchedule.getCategory())
                .visibility(singleSchedule.getVisibility())
                .startDateTime(singleSchedule.getStartDateTime())
                .endDateTime(singleSchedule.getEndDateTime())
                .build();
    }
}