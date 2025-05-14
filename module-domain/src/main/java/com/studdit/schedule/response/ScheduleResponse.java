package com.studdit.schedule.response;

import com.studdit.schedule.repository.Schedule;
import com.studdit.schedule.request.ScheduleCreateServiceRequest;
import com.studdit.schedule.enums.Visibility;
import com.studdit.schedule.request.ScheduleModifyServiceRequest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Visibility visibility;
    private boolean reviewWritten;
    private boolean verification;

    @Builder
    private ScheduleResponse(
            Long id,
            String title,
            String description,
            String category,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Visibility visibility,
            boolean reviewWritten,
            boolean verification
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.visibility = visibility;
        this.reviewWritten = reviewWritten;
        this.verification = verification;
    }

    public static ScheduleResponse of(Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .category(schedule.getCategory())
                .startDateTime(schedule.getStartDateTime())
                .endDateTime(schedule.getEndDateTime())
                .reviewWritten(false)
                .verification(false)
                .visibility(schedule.getVisibility())
                .build();
    }
}
