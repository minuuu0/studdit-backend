package com.studdit.schedule.response;

import com.studdit.schedule.domain.Schedule;
import com.studdit.schedule.domain.ScheduleInstance;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ScheduleCreateResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private List<ScheduleInstanceResponse> instances;

    @Builder
    private ScheduleCreateResponse(
            Long id,
            String title,
            String description,
            String category,
            List<ScheduleInstanceResponse> instances
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.instances = instances;
    }

    public static ScheduleCreateResponse of(Schedule schedule, List<ScheduleInstance> instances) {
        return ScheduleCreateResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .category(schedule.getCategory())
                .instances(instances.stream()
                        .map(ScheduleInstanceResponse::of)
                        .collect(Collectors.toList()))
                .build();
    }
}