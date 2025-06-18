package com.studdit.recurringschedule.response;

import com.studdit.recurringschedule.RecurrenceRule;
import com.studdit.schedule.enums.RecurrenceType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Getter
@NoArgsConstructor
public class RecurrenceRuleCreateResponse {

    private Long id;

    private RecurrenceType recurrenceType;


    @Builder
    private RecurrenceRuleCreateResponse(
            Long id,
            RecurrenceType recurrenceType
    ) {
        this.id = id;
        this.recurrenceType = recurrenceType;
    }

    public static RecurrenceRuleCreateResponse of(RecurrenceRule recurrenceRule) {
        return RecurrenceRuleCreateResponse.builder()
                .id(recurrenceRule.getId())
                .recurrenceType(recurrenceRule.getRecurrenceType())
                .build();
    }
}
