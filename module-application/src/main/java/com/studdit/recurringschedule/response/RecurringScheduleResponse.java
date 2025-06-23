package com.studdit.recurringschedule.response;

import com.studdit.recurringschedule.RecurringSchedule;
import com.studdit.schedule.enums.Visibility;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecurringScheduleResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private Visibility visibility;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Long recurrenceRuleId;

    @Builder
    private RecurringScheduleResponse(
            Long id,
            String title,
            String description,
            String category,
            Visibility visibility,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Long recurrenceRuleId
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.recurrenceRuleId = recurrenceRuleId;
    }

    public static RecurringScheduleResponse of(RecurringSchedule recurringSchedule) {
        return RecurringScheduleResponse.builder()
                .id(recurringSchedule.getId())
                .title(recurringSchedule.getTitle())
                .description(recurringSchedule.getDescription())
                .category(recurringSchedule.getCategory())
                .visibility(recurringSchedule.getVisibility())
                .startDateTime(recurringSchedule.getStartDateTime())
                .endDateTime(recurringSchedule.getEndDateTime())
                .recurrenceRuleId(recurringSchedule.getRecurrenceRuleId())
                .build();
    }
}