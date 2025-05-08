package com.studdit.schedule.request;

import com.studdit.schedule.enums.Visibility;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleModifyServiceRequest {

    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Visibility visibility;


    @Builder
    private ScheduleModifyServiceRequest(
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
}
