package com.studdit.recurringschedule.request;

import com.studdit.recurringschedule.RecurrenceRule;
import com.studdit.schedule.enums.DayofWeek;
import com.studdit.schedule.enums.RecurrenceType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Getter
@NoArgsConstructor
public class RecurrenceRuleCreateRequest {

    private Long recurringScheduleId;

    private RecurrenceType recurrenceType;

    private Integer frequency; // 반복주기

    private Set<DayofWeek> byday;   // 반복 요일

    private Integer byMonthday; // 반복 날짜

    private Integer byMonth;    // 반복 월

    private LocalDate endDate;  // 반복 종료 날짜

    private Integer maxOccurrences; // 최대 반복 횟수

    @Builder
    private RecurrenceRuleCreateRequest(
            Long recurringScheduleId,
            RecurrenceType recurrenceType,
            Integer frequency, Set<DayofWeek> byday,
            Integer byMonthday,
            Integer byMonth,
            LocalDate endDate,
            Integer maxOccurrences
    ) {
        this.recurringScheduleId = recurringScheduleId;
        this.recurrenceType = recurrenceType;
        this.frequency = frequency;
        this.byday = byday;
        this.byMonthday = byMonthday;
        this.byMonth = byMonth;
        this.endDate = endDate;
        this.maxOccurrences = maxOccurrences;
    }

    public RecurrenceRule toRecurrenceRuleEntity() {
        return RecurrenceRule.builder()
                .recurringScheduleId(recurringScheduleId)
                .recurrenceType(recurrenceType)
                .frequency(frequency)
                .byday(byday)
                .byMonthday(byMonthday)
                .byMonth(byMonth)
                .endDate(endDate)
                .maxOccurrences(maxOccurrences)
                .build();
    }

}
