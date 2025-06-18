package com.studdit.recurringschedule.request;

import com.studdit.recurringschedule.RecurringSchedule;
import com.studdit.schedule.enums.Visibility;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RecurringScheduleCreateRequest {

    private String title;

    @Column(length = 1000)
    private String description;

    private String category;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    private RecurrenceRuleCreateRequest recurrenceRuleCreateRequest;

    @Builder
    private RecurringScheduleCreateRequest(
            String title,
            String description,
            String category,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Visibility visibility,
            RecurrenceRuleCreateRequest recurrenceRuleCreateRequest
    ) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.visibility = visibility;
        this.recurrenceRuleCreateRequest = recurrenceRuleCreateRequest;
    }

    public RecurringSchedule toRecurringScheduleEntity() {
        return RecurringSchedule.builder()
                .title(title)
                .description(description)
                .category(category)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .visibility(visibility)
                .build();
    }

}
