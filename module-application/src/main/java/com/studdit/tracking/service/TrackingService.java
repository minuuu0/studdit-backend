package com.studdit.tracking.service;

import com.studdit.tracking.enums.TrackingStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.studdit.tracking.enums.TrackingStatus.IN_PROGRESS;

@Service
public class TrackingService {
    public TrackingResponse createTracking(Long scheduleId) {
        return TrackingResponse.builder()
                .id(1L)
                .eventTime(LocalDateTime.now())
                .status(IN_PROGRESS)
                .scheduleId(scheduleId)
                .build();
    }

    public TrackingResponse modifyTracking(Long scheduleId, Long trackingId, TrackingStatus trackingStatus) {
        return TrackingResponse.builder()
                .id(trackingId)
                .scheduleId(scheduleId)
                .status(trackingStatus)
                .eventTime(LocalDateTime.now())
                .build();
    }

}
