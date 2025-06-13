package com.studdit.recurringschedule.service;

import com.studdit.recurringschedule.RecurrenceRule;
import com.studdit.recurringschedule.RecurringSchedule;
import com.studdit.recurringschedule.request.RecurringScheduleCreateRequest;
import com.studdit.schedule.enums.DayofWeek;
import com.studdit.schedule.enums.RecurrenceType;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class RecurringScheduleGenerator {
    private static final int DEFAULT_SCHEDULE_GENERATION_MONTHS = 3;
    private static final int DEFAULT_MAX_INSTANCES = 1000;

    public List<RecurringSchedule> createRecurringSchedule(
            RecurringScheduleCreateRequest request,
            RecurrenceRule recurrenceRule
    ) {
        List<RecurringSchedule> schedules = new ArrayList<>();
        LocalDateTime currentDateTime = request.getStartDateTime();
        LocalDateTime generationUntil = currentDateTime.plusMonths(DEFAULT_SCHEDULE_GENERATION_MONTHS);
        Duration duration = Duration.between(request.getStartDateTime(), request.getEndDateTime());
        
        int count = 0;
        
        while (currentDateTime.isBefore(generationUntil) && 
               shouldContinueGenerating(recurrenceRule, count, currentDateTime)) {
            
            // 요일 기반 반복 처리
            if (shouldIncludeDate(currentDateTime, recurrenceRule)) {
                schedules.add(RecurringSchedule.builder()
                        .recurrenceRuleId(recurrenceRule.getId())
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .category(request.getCategory())
                        .startDateTime(currentDateTime)
                        .endDateTime(currentDateTime.plus(duration))
                        .visibility(request.getVisibility())
                        .build());
                count++;
            }
            
            currentDateTime = calculateNextOccurrence(currentDateTime, recurrenceRule);
        }
        
        return schedules;
    }

    private boolean shouldIncludeDate(LocalDateTime dateTime, RecurrenceRule rule) {
        // 요일 기반 반복 처리
        if (rule.getRecurrenceType() == RecurrenceType.WEEKLY && rule.getByday() != null && !rule.getByday().isEmpty()) {
            DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
            Set<DayofWeek> byDays = rule.getByday();
            
            // 현재 날짜의 요일이 반복 요일 목록에 포함되어 있는지 확인
            return byDays.stream()
                    .anyMatch(day -> day.name().substring(0, 2).equalsIgnoreCase(dayOfWeek.name().substring(0, 2)));
        }
        
        // 월 기반 반복 처리
        if (rule.getRecurrenceType() == RecurrenceType.MONTHLY && rule.getByMonthday() != null) {
            return dateTime.getDayOfMonth() == rule.getByMonthday();
        }
        
        // 년 기반 반복 처리
        if (rule.getRecurrenceType() == RecurrenceType.YEARLY && rule.getByMonth() != null) {
            return dateTime.getMonthValue() == rule.getByMonth();
        }
        
        return true;
    }

    private LocalDateTime calculateNextOccurrence(LocalDateTime currentDateTime, RecurrenceRule rule) {
        switch (rule.getRecurrenceType()) {
            case DAILY:
                return currentDateTime.plusDays(rule.getFrequency() != null ? rule.getFrequency() : 1);
            case WEEKLY:
                if (rule.getByday() != null && !rule.getByday().isEmpty()) {
                    // 요일 기반 반복인 경우 다음 날로 이동
                    return currentDateTime.plusDays(1);
                } else {
                    // 주 기반 반복인 경우 주 단위로 이동
                    return currentDateTime.plusWeeks(rule.getFrequency() != null ? rule.getFrequency() : 1);
                }
            case MONTHLY:
                return currentDateTime.plusMonths(rule.getFrequency() != null ? rule.getFrequency() : 1);
            case YEARLY:
                return currentDateTime.plusYears(rule.getFrequency() != null ? rule.getFrequency() : 1);
            default:
                return currentDateTime.plusDays(1);
        }
    }

    private boolean shouldContinueGenerating(RecurrenceRule rule, int currentCount, LocalDateTime currentDateTime) {
        // 최대 발생 횟수 체크
        if (rule.getMaxOccurrences() != null && currentCount >= rule.getMaxOccurrences()) {
            return false;
        }
        
        // 종료 날짜 체크
        if (rule.getEndDate() != null && currentDateTime.isAfter(rule.getEndDate().atStartOfDay())) {
            return false;
        }
        
        // 무한 반복 방지
        if (currentCount >= DEFAULT_MAX_INSTANCES) {
            return false;
        }
        
        return true;
    }
}