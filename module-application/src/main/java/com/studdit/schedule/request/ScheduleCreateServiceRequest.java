package com.studdit.schedule.request;

import com.studdit.schedule.enums.Visibility;
import com.studdit.schedule.SingleSchedule;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScheduleCreateServiceRequest {

    private String title;
    private String description;
    private String category;
    private Visibility visibility;
    private LocalDateTime startDateTime;     // 시작 일시
    private LocalDateTime endDateTime;       // 종료 일시

    @Builder
    private ScheduleCreateServiceRequest(
            String title,
            String description,
            String category,
            Visibility visibility,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public SingleSchedule toEntity() {
        return SingleSchedule.builder()
                .title(title)
                .description(description)
                .category(category)
                .visibility(visibility)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();
    }
}
