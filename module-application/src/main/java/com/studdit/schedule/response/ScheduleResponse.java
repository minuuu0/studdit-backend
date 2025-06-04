package com.studdit.schedule.response;

import com.studdit.schedule.domain.Schedule;
import com.studdit.schedule.domain.ScheduleInstance;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ScheduleResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private boolean isRecurring;
    private List<ScheduleInstanceResponse> instances;

    @Builder
    private ScheduleResponse(
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

    public static ScheduleResponse of(ScheduleInstance instance, Schedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .category(schedule.getCategory())
                .isRecurring(schedule.getIsRecurring())
                .instances(List.of(ScheduleInstanceResponse.of(instance)))
                .build();
    }

    public static ScheduleResponse of(Schedule schedule, List<ScheduleInstance> instances) {

        return ScheduleResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .category(schedule.getCategory())
                .isRecurring(schedule.getIsRecurring())
                .instances(instances.stream()
                        .map(ScheduleInstanceResponse::of)
                        .collect(Collectors.toList()))
                .build();
    }


    public static List<ScheduleResponse> fromInstances(List<ScheduleInstance> instances, Map<Long, Schedule> scheduleMap) {
        // 1. 같은 스케줄끼리 그룹화
        Map<Long, List<ScheduleInstance>> instancesByScheduleId = instances.stream()
                .collect(Collectors.groupingBy(ScheduleInstance::getScheduleId));

        // 2. 스케줄 변환
        return instancesByScheduleId.keySet().stream()
                .map(scheduleId -> {
                    List<ScheduleInstance> scheduleInstances = instancesByScheduleId.get(scheduleId);
                    Schedule schedule = scheduleMap.get(scheduleId);
                    return of(schedule, scheduleInstances);
                })
                .collect(Collectors.toList());
    }
}
