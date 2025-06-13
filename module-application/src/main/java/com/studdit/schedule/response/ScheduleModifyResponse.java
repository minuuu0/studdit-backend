package com.studdit.schedule.response;

import com.studdit.schedule.domain.Schedule;
import com.studdit.schedule.domain.ScheduleInstance;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ScheduleModifyResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private boolean isRecurring;
    private List<ScheduleInstanceResponse> instances;

    @Builder
    private ScheduleModifyResponse(
            Long id,
            String title,
            String description,
            String category,
            boolean isRecurring,
            List<ScheduleInstanceResponse> instances
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.isRecurring = isRecurring;
        this.instances = instances;
    }

    public static ScheduleModifyResponse of(ScheduleInstance instance, Schedule schedule) {
        return ScheduleModifyResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .category(schedule.getCategory())
                .isRecurring(schedule.getIsRecurring())
                .instances(List.of(ScheduleInstanceResponse.of(instance)))
                .build();
    }
}