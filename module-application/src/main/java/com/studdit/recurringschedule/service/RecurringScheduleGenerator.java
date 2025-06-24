package com.studdit.recurringschedule.service;

import com.studdit.recurringschedule.RecurrenceRule;
import com.studdit.recurringschedule.RecurringSchedule;
import com.studdit.recurringschedule.request.RecurringScheduleCreateRequest;
import com.studdit.schedule.enums.RecurrenceType;
import com.studdit.schedule.enums.Visibility;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class RecurringScheduleGenerator {
    private static final int DEFAULT_MAX_OCCURRENCES = 1000;

    public List<RecurringSchedule> createRecurringSchedule(
            RecurringScheduleCreateRequest request, 
            RecurrenceRule rule) {
        
        return createSchedules(request, rule);
    }
    
    
    private List<RecurringSchedule> createSchedules(
            RecurringScheduleCreateRequest request,
            RecurrenceRule rule) {
        
        List<RecurringSchedule> schedules = new ArrayList<>();
        LocalDateTime currentStart = request.getStartDateTime();
        LocalDateTime currentEnd = request.getEndDateTime();
        
        int maxOccurrences = rule.getMaxOccurrences() != null ? rule.getMaxOccurrences() : DEFAULT_MAX_OCCURRENCES;
        LocalDate endDate = rule.getEndDate();
        
        // endDate가 startDate보다 이전인 경우 예외 발생
        if (endDate != null && endDate.isBefore(currentStart.toLocalDate())) {
            throw new IllegalArgumentException("반복 종료일은 시작일보다 이후여야 합니다.");
        }

        String title = request.getTitle();
        String description = request.getDescription();
        String category = request.getCategory();
        Visibility visibility = request.getVisibility();
        RecurrenceType type = rule.getRecurrenceType();

        for (int i = 0; i < maxOccurrences; i++) {
            if (endDate != null && currentStart.toLocalDate().isAfter(endDate)) {
                break;
            }
            
            RecurringSchedule schedule = RecurringSchedule.builder()
                    .recurrenceRuleId(rule.getId())
                    .title(title)
                    .description(description)
                    .category(category)
                    .startDateTime(currentStart)
                    .endDateTime(currentEnd)
                    .visibility(visibility)
                    .build();
                    
            schedules.add(schedule);
            
            // 반복 타입에 따라 다음 날짜로 이동
            currentStart = getNextDateTime(currentStart, type);
            currentEnd = getNextDateTime(currentEnd, type);
        }
        
        return schedules;
    }
    
    private LocalDateTime getNextDateTime(LocalDateTime dateTime, RecurrenceType type) {
        switch (type) {
            case DAILY:
                return dateTime.plusDays(1);
            case WEEKLY:
                return dateTime.plusWeeks(1);
            case MONTHLY:
                return dateTime.plusMonths(1);
            case YEARLY:
                return dateTime.plusYears(1);
            default:
                throw new IllegalArgumentException("지원하지 않는 반복타입 : " + type);
        }
    }
}