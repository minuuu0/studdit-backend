package com.studdit.schedule.request;

import com.studdit.schedule.enums.Visibility;
import com.studdit.schedule.domain.Schedule;
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
    private RecurrenceRuleCreateServiceRequest recurrenceRuleCreateServiceRequest;

    @Builder
    private ScheduleCreateServiceRequest(
            String title,
            String description,
            String category,
            Visibility visibility,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            RecurrenceRuleCreateServiceRequest recurrenceRuleCreateServiceRequest
    ) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.recurrenceRuleCreateServiceRequest = recurrenceRuleCreateServiceRequest;
    }

    public Schedule toScheduleEntity() {
        boolean isRecurring = this.recurrenceRuleCreateServiceRequest != null;
        return Schedule.builder()
                .title(title)
                .description(description)
                .category(category)
                .isRecurring(isRecurring)
                .build();
    }
}
