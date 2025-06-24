package com.studdit.recurringschedule.request;

import com.studdit.recurringschedule.RecurrenceRule;
import com.studdit.schedule.enums.RecurrenceType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RecurrenceRuleModifyRequest {

    private RecurrenceType recurrenceType;

    private Integer maxOccurrences;

    private LocalDate endDate;

    @Builder
    private RecurrenceRuleModifyRequest(
            RecurrenceType recurrenceType,
            Integer maxOccurrences,
            LocalDate endDate
    ) {
        this.recurrenceType = recurrenceType;
        this.maxOccurrences = maxOccurrences;
        this.endDate = endDate;
    }

    public RecurrenceRule toRecurrenceRuleEntity() {
        return RecurrenceRule.builder()
                .recurrenceType(recurrenceType)
                .maxOccurrences(maxOccurrences)
                .endDate(endDate)
                .build();
    }
}
