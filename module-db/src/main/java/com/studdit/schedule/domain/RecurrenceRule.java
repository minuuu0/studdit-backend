package com.studdit.schedule.domain;

import com.studdit.BaseEntity;
import com.studdit.schedule.enums.RecurrenceType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class RecurrenceRule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long ScheduleId;

    @Enumerated(EnumType.STRING)
    private RecurrenceType type;

    private Integer frequency; // 반복주기 n일/주/월/년

    private String byWeekday;   // 어떤 요일에 반복되는지 지정

    private Integer byMonthday; // 월의 몇 번째 날에 반복되는지 지정

    private Integer byMonth;    // 연도의 몇 번째 월에 반복되는지 지정

    private LocalDate endDate;  // 반복 종료 날짜

    private Integer maxOccurrences; // 최대 반복 횟수

    @Builder
    private RecurrenceRule(
            Long id,
            Long scheduleId,
            RecurrenceType type,
            Integer frequency,
            String byWeekday,
            Integer byMonthday,
            Integer byMonth,
            LocalDate endDate,
            Integer maxOccurrences
    ) {
        this.id = id;
        ScheduleId = scheduleId;
        this.type = type;
        this.frequency = frequency;
        this.byWeekday = byWeekday;
        this.byMonthday = byMonthday;
        this.byMonth = byMonth;
        this.endDate = endDate;
        this.maxOccurrences = maxOccurrences;
    }
}
