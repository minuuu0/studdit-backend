package com.studdit.schedule.request;

import com.studdit.schedule.domain.Schedule;
import com.studdit.schedule.domain.ScheduleInstance;
import com.studdit.schedule.enums.ScheduleModifyType;
import com.studdit.schedule.enums.Visibility;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleModifyServiceRequest {

    private Long scheduleId;
    private Long instanceId;
    private String title;
    private String description;
    private String category;
    private Visibility visibility;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private RecurrenceRuleCreateServiceRequest recurrenceRuleCreateServiceRequest;
    private ScheduleModifyType modifyType;

    @Builder
    private ScheduleModifyServiceRequest(
            Long scheduleId,
            Long instanceId,
            String title,
            String description,
            String category,
            Visibility visibility,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            RecurrenceRuleCreateServiceRequest recurrenceRuleCreateServiceRequest,
            ScheduleModifyType modifyType
    ) {
        this.scheduleId = scheduleId;
        this.instanceId = instanceId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.recurrenceRuleCreateServiceRequest = recurrenceRuleCreateServiceRequest;
        this.modifyType = modifyType;
    }

    public Schedule toScheduleEntity() {
        boolean isRecurring = this.recurrenceRuleCreateServiceRequest != null;
        return Schedule.builder()
                .id(scheduleId)
                .title(title)
                .description(description)
                .category(category)
                .isRecurring(isRecurring)
                .build();
    }

    public ScheduleInstance toScheduleInstanceEntity() {
        return ScheduleInstance.builder()
                .id(instanceId)
                .visibility(visibility)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();
    }

}
