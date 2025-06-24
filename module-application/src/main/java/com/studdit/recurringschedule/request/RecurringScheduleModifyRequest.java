package com.studdit.recurringschedule.request;

import com.studdit.schedule.enums.RecurrenceEditType;
import com.studdit.schedule.enums.Visibility;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RecurringScheduleModifyRequest {

    private String title;
    
    private String description;

    private String category;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private Visibility visibility;
    
    private RecurrenceEditType editType;

    private RecurrenceRuleModifyRequest recurrenceRuleModifyRequest;

    @Builder
    private RecurringScheduleModifyRequest(
            String title,
            String description,
            String category,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Visibility visibility,
            RecurrenceEditType editType,
            RecurrenceRuleModifyRequest recurrenceRuleModifyRequest
    ) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.startDateTime = startDateTime;  
        this.endDateTime = endDateTime;
        this.visibility = visibility;
        this.editType = editType;
        this.recurrenceRuleModifyRequest = recurrenceRuleModifyRequest;
    }
}