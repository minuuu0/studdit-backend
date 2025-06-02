package com.studdit.tracking.service;

import com.studdit.tracking.enums.TrackingStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TrackingResponse {

    private Long id;
    private Long scheduleId;
    private LocalDateTime eventTime;
    private TrackingStatus status;

    @Builder
    private TrackingResponse(
            Long id,
            Long scheduleId,
            LocalDateTime eventTime,
            TrackingStatus status
    ) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.eventTime = eventTime;
        this.status = status;
    }


}
