package com.studdit.schedule.service;

import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;

@Getter
public class DateRange {

    private final LocalDateTime start;
    private final LocalDateTime end;

    @Builder
    private DateRange(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public static DateRange ofDay(LocalDateTime date) {
        LocalDateTime start = date.toLocalDate().atStartOfDay();
        LocalDateTime end = date.toLocalDate().atTime(LocalTime.MAX);

        return DateRange.builder()
                .start(start)
                .end(end)
                .build();
    }

    public static DateRange ofWeek(LocalDateTime date) {
        LocalDate start = date.toLocalDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate end = date.toLocalDate().with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        return DateRange.builder()
                .start(start.atStartOfDay())
                .end(end.atTime(LocalTime.MAX))
                .build();
    }


    public static DateRange ofMonth(LocalDateTime date) {
        // 해당 월의 첫날과 마지막날 구하기
        LocalDate firstDayOfMonth = date.toLocalDate().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = date.toLocalDate().with(TemporalAdjusters.lastDayOfMonth());

        // 해당 월의 첫날이 속한 주의 일요일(주의 시작일) 찾기
        LocalDate start = firstDayOfMonth.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        // 해당 월의 마지막날이 속한 주의 토요일(주의 마지막일) 찾기
        LocalDate end = lastDayOfMonth.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        return DateRange.builder()
                .start(start.atStartOfDay())
                .end(end.atTime(LocalTime.MAX))
                .build();
    }
}
