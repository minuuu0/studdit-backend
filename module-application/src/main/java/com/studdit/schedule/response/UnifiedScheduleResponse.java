package com.studdit.schedule.response;

import com.studdit.recurringschedule.RecurringSchedule;
import com.studdit.schedule.SingleSchedule;
import com.studdit.schedule.enums.Visibility;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UnifiedScheduleResponse {
    
    private Long id;
    private String title;
    private String description;
    private String category;
    private Visibility visibility;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private ScheduleType scheduleType;
    private Long recurrenceRuleId; // 반복 일정인 경우에만 값이 있음
    
    @Builder
    private UnifiedScheduleResponse(Long id, String title, String description, String category, 
                                   Visibility visibility, LocalDateTime startDateTime, 
                                   LocalDateTime endDateTime, ScheduleType scheduleType, 
                                   Long recurrenceRuleId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.scheduleType = scheduleType;
        this.recurrenceRuleId = recurrenceRuleId;
    }
    
    public static UnifiedScheduleResponse fromSingleSchedule(SingleSchedule singleSchedule) {
        return UnifiedScheduleResponse.builder()
                .id(singleSchedule.getId())
                .title(singleSchedule.getTitle())
                .description(singleSchedule.getDescription())
                .category(singleSchedule.getCategory())
                .visibility(singleSchedule.getVisibility())
                .startDateTime(singleSchedule.getStartDateTime())
                .endDateTime(singleSchedule.getEndDateTime())
                .scheduleType(ScheduleType.SINGLE)
                .recurrenceRuleId(null)
                .build();
    }
    
    public static UnifiedScheduleResponse fromRecurringSchedule(RecurringSchedule recurringSchedule) {
        return UnifiedScheduleResponse.builder()
                .id(recurringSchedule.getId())
                .title(recurringSchedule.getTitle())
                .description(recurringSchedule.getDescription())
                .category(recurringSchedule.getCategory())
                .visibility(recurringSchedule.getVisibility())
                .startDateTime(recurringSchedule.getStartDateTime())
                .endDateTime(recurringSchedule.getEndDateTime())
                .scheduleType(ScheduleType.RECURRING)
                .recurrenceRuleId(recurringSchedule.getRecurrenceRuleId())
                .build();
    }
    
    public enum ScheduleType {
        SINGLE,    // 단일 일정
        RECURRING  // 반복 일정
    }
}