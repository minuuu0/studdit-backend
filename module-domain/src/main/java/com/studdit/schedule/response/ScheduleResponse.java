package com.studdit.schedule.response;

import com.studdit.schedule.request.ScheduleCreateServiceRequest;
import com.studdit.schedule.enums.Visibility;
import com.studdit.schedule.request.ScheduleModifyServiceRequest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Visibility visibility;
    private boolean reviewWritten;
    private boolean verification;

    @Builder
    private ScheduleResponse(
            Long id,
            String title,
            String description,
            String category,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Visibility visibility,
            boolean reviewWritten,
            boolean verification
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.startTime = startTime;
        this.endTime = endTime;
        this.visibility = visibility;
        this.reviewWritten = reviewWritten;
        this.verification = verification;
    }

    public static ScheduleResponse of(ScheduleCreateServiceRequest serviceRequest) {
        return ScheduleResponse.builder()
                .id(1L)
                .title(serviceRequest.getTitle())
                .description(serviceRequest.getDescription())
                .category(serviceRequest.getCategory())
                .startTime(serviceRequest.getStartDateTime())
                .endTime(serviceRequest.getEndDateTime())
                .reviewWritten(false)
                .verification(false)
                .build();
    }

    public static ScheduleResponse of(ScheduleModifyServiceRequest serviceRequest) {
        return ScheduleResponse.builder()
                .id(serviceRequest.getId())
                .title(serviceRequest.getTitle())
                .description(serviceRequest.getDescription())
                .category(serviceRequest.getCategory())
                .startTime(serviceRequest.getStartDateTime())
                .endTime(serviceRequest.getEndDateTime())
                .reviewWritten(false)
                .verification(false)
                .build();
    }
}
