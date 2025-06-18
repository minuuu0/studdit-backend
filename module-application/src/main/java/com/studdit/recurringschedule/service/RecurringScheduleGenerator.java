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
        
        List<RecurringSchedule> schedules = new ArrayList<>();
        
        if (rule.getRecurrenceType() == RecurrenceType.DAILY) {
            return createDailySchedules(request, rule);
        }
        
        return schedules;
    }
    
    private List<RecurringSchedule> createDailySchedules(
            RecurringScheduleCreateRequest request,
            RecurrenceRule rule) {
        
        List<RecurringSchedule> schedules = new ArrayList<>();
        LocalDateTime currentStart = request.getStartDateTime();
        LocalDateTime currentEnd = request.getEndDateTime();
        
        int maxOccurrences = rule.getMaxOccurrences() != null ? rule.getMaxOccurrences() : DEFAULT_MAX_OCCURRENCES;
        LocalDate endDate = rule.getEndDate();

        String title = request.getTitle();
        String description = request.getDescription();
        String category = request.getCategory();
        Visibility visibility = request.getVisibility();

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
            
            // 다음 날로 이동
            currentStart = currentStart.plusDays(1);
            currentEnd = currentEnd.plusDays(1);
        }
        
        return schedules;
    }
}