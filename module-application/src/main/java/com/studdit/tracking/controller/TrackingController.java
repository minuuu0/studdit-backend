package com.studdit.tracking.controller;

import com.studdit.ApiResponse;
import com.studdit.tracking.enums.TrackingStatus;
import com.studdit.tracking.service.TrackingResponse;
import com.studdit.tracking.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @PostMapping("/schedules/{scheduleId}/track/start")
    private ApiResponse<TrackingResponse> createTracking(@PathVariable Long scheduleId) {
        return ApiResponse.ok(trackingService.createTracking(scheduleId));
    }

    @PostMapping("/schedules/{scheduleId}/track/{trackingId}/stop")
    private ApiResponse<TrackingResponse> pauseTracking(@PathVariable Long scheduleId, @PathVariable Long trackingId) {
        return ApiResponse.ok(trackingService.modifyTracking(scheduleId, trackingId, TrackingStatus.PAUSED));
    }

    @PostMapping("/schedules/{scheduleId}/track/{trackingId}/resume")
    private ApiResponse<TrackingResponse> resumeTracking(@PathVariable Long scheduleId, @PathVariable Long trackingId) {
        return ApiResponse.ok(trackingService.modifyTracking(scheduleId, trackingId, TrackingStatus.IN_PROGRESS));
    }

    @PostMapping("/schedules/{scheduleId}/track/{trackingId}/complete")
    private ApiResponse<TrackingResponse> completeTracking(@PathVariable Long scheduleId, @PathVariable Long trackingId) {
        return ApiResponse.ok(trackingService.modifyTracking(scheduleId, trackingId, TrackingStatus.COMPLETED));
    }
}
