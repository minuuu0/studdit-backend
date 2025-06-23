package com.studdit.recurringschedule.request;

import com.studdit.recurringschedule.RecurrenceRule;
import com.studdit.schedule.enums.RecurrenceType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RecurrenceRuleCreateRequest {

    private RecurrenceType recurrenceType;

    private Integer maxOccurrences;

    private LocalDate endDate;

    @Builder
    private RecurrenceRuleCreateRequest(
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
