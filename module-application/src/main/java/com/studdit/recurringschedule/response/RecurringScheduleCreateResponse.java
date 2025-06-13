package com.studdit.recurringschedule.response;

import com.studdit.recurringschedule.RecurrenceRule;
import com.studdit.recurringschedule.RecurringSchedule;
import com.studdit.schedule.response.ScheduleCreateResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RecurringScheduleCreateResponse {

    private RecurrenceRuleCreateResponse recurrenceRule;
    private List<RecurringSchedule> schedules;

    @Builder
    private RecurringScheduleCreateResponse(
            RecurrenceRuleCreateResponse recurrenceRule,
            List<RecurringSchedule> schedules
    ) {
        this.recurrenceRule = recurrenceRule;
        this.schedules = schedules;
    }

    public static RecurringScheduleCreateResponse of(List<RecurringSchedule> savedSchedules, RecurrenceRule savedRule) {
        return RecurringScheduleCreateResponse.builder()
                .recurrenceRule(RecurrenceRuleCreateResponse.of(savedRule))
                .schedules(savedSchedules)
                .build();
    }
}