package com.studdit.recurringschedule.response;

import com.studdit.recurringschedule.RecurrenceRule;
import com.studdit.recurringschedule.RecurringSchedule;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class RecurringScheduleCreateResponse {

    private RecurrenceRuleCreateResponse recurrenceRule;
    private List<RecurringScheduleResponse> schedules;

    @Builder
    private RecurringScheduleCreateResponse(
            RecurrenceRuleCreateResponse recurrenceRule,
            List<RecurringScheduleResponse> schedules
    ) {
        this.recurrenceRule = recurrenceRule;
        this.schedules = schedules;
    }

    public static RecurringScheduleCreateResponse of(List<RecurringSchedule> savedSchedules, RecurrenceRule savedRule) {
        return RecurringScheduleCreateResponse.builder()
                .recurrenceRule(RecurrenceRuleCreateResponse.of(savedRule))
                .schedules(savedSchedules.stream()
                        .map(RecurringScheduleResponse::of)
                        .collect(Collectors.toList()))
                .build();
    }
}