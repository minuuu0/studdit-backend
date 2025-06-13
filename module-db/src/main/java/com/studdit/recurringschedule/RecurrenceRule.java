package com.studdit.recurringschedule;

import com.studdit.BaseEntity;
import com.studdit.schedule.enums.DayofWeek;
import com.studdit.schedule.enums.RecurrenceType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class RecurrenceRule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long recurringScheduleId;

    @Enumerated(EnumType.STRING)
    private RecurrenceType recurrenceType;

    private Integer frequency; // 반복주기

    @Enumerated(EnumType.STRING)
    @ElementCollection
    private Set<DayofWeek> byday;   // 반복 요일

    private Integer byMonthday; // 반복 날짜

    private Integer byMonth;    // 반복 월

    private LocalDate endDate;  // 반복 종료 날짜

    private Integer maxOccurrences; // 최대 반복 횟수

    @Builder
    private RecurrenceRule(
            Long id,
            Long recurringScheduleId,
            RecurrenceType recurrenceType,
            Integer frequency,
            Set<DayofWeek> byday,
            Integer byMonthday,
            Integer byMonth,
            LocalDate endDate,
            Integer maxOccurrences
    ) {
        this.id = id;
        this.recurringScheduleId = recurringScheduleId;
        this.recurrenceType = recurrenceType;
        this.frequency = frequency;
        this.byday = byday;
        this.byMonthday = byMonthday;
        this.byMonth = byMonth;
        this.endDate = endDate;
        this.maxOccurrences = maxOccurrences;
    }
}
