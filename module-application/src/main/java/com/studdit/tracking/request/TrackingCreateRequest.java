package com.studdit.tracking.request;

import com.studdit.tracking.enums.TrackingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TrackingCreateRequest {

    private Long scheduleId;
    private LocalDateTime start;
    private TrackingStatus trackingStatus;
}
