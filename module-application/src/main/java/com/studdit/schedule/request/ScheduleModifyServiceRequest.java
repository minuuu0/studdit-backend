package com.studdit.schedule.request;

import com.studdit.schedule.domain.Schedule;
import com.studdit.schedule.enums.Visibility;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ScheduleModifyServiceRequest {

    private Long id;
    private String title;
    private String description;
    private String category;
    private Visibility visibility;
    private boolean isRecurring;

    @Builder
    private ScheduleModifyServiceRequest(
            Long id,
            String title,
            String description,
            String category,
            Visibility visibility,
            boolean isRecurring
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
        this.isRecurring = isRecurring;
    }

    public Schedule toEntity() {
        return Schedule.builder()
                .id(id)
                .title(title)
                .description(description)
                .category(category)
                .visibility(visibility)
                .isRecurring(isRecurring)
                .build();

    }
}
