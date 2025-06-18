package com.studdit.schedule.response;

import com.studdit.schedule.SingleSchedule;
import com.studdit.schedule.enums.Visibility;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleCreateResponse {

    private Long id;

    private String title;

    private String description;

    private String category;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Builder
    private ScheduleCreateResponse(
            Long id,
            String title,
            String description,
            String category,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Visibility visibility
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.visibility = visibility;
    }

    public static ScheduleCreateResponse of(SingleSchedule singleSchedule) {
        return ScheduleCreateResponse.builder()
                .id(singleSchedule.getId())
                .title(singleSchedule.getTitle())
                .description(singleSchedule.getDescription())
                .category(singleSchedule.getCategory())
                .startDateTime(singleSchedule.getStartDateTime())
                .endDateTime(singleSchedule.getEndDateTime())
                .visibility(singleSchedule.getVisibility())
                .build();
    }
}