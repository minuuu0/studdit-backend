package com.studdit.schedule.response;

import com.studdit.schedule.SingleSchedule;
import com.studdit.schedule.enums.Visibility;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private Visibility visibility;
    private LocalDateTime startDateTime;     // 시작 일시
    private LocalDateTime endDateTime;       // 종료 일시

    @Builder
    private ScheduleResponse(
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

    public static ScheduleResponse of(SingleSchedule singleSchedule) {
        return ScheduleResponse.builder()
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
