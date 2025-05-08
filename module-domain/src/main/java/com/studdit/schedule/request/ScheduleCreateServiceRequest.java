package com.studdit.schedule.request;

import com.studdit.schedule.enums.Visibility;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
public class ScheduleCreateServiceRequest {

    private String title;
    private String description;
    private String category;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Visibility visibility;

    @Builder
    private ScheduleCreateServiceRequest(String title,
                                        String description,
                                        String category,
                                        LocalDateTime startDateTime,
                                        LocalDateTime endDateTime,
                                        Visibility visibility
    ) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.visibility = visibility;
    }
}
