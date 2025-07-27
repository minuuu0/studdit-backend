package com.studdit.schedule.controller;

import com.studdit.ApiResponse;
import com.studdit.schedule.request.ScheduleCreateRequest;
import com.studdit.schedule.request.ScheduleModifyRequest;
import com.studdit.schedule.response.ScheduleCreateResponse;
import com.studdit.schedule.response.ScheduleResponse;
import com.studdit.schedule.response.ScheduleModifyResponse;
import com.studdit.schedule.response.UnifiedScheduleResponse;
import com.studdit.schedule.service.ScheduleService;
import com.studdit.schedule.service.UnifiedScheduleQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final UnifiedScheduleQueryService unifiedScheduleQueryService;

    @PostMapping()
    private ApiResponse<ScheduleCreateResponse> createSchedule(
            @Valid @RequestBody ScheduleCreateRequest request
    ) {
        return ApiResponse.ok(scheduleService.createSchedule(request.toServiceRequest()));
    }

    @GetMapping()
    private ApiResponse<List<UnifiedScheduleResponse>> findSchedules(
            @RequestParam String username,
            @RequestParam(required = false, defaultValue = "month") String view,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date
    ) {
        return ApiResponse.ok(unifiedScheduleQueryService.findSchedules(username, view, date));
    }

    @PutMapping("/{scheduleId}")
    private ApiResponse<ScheduleModifyResponse> modifySchedule(
            @PathVariable Long scheduleId,
            @RequestBody ScheduleModifyRequest request) {
        return ApiResponse.ok(scheduleService.modifySchedule(request.toServiceRequest(scheduleId)));
    }

    @DeleteMapping("/{id}")
    private ApiResponse<ScheduleResponse> deleteSchedule(@Valid @PathVariable Long id) {
        return ApiResponse.ok(scheduleService.deleteSchedule(id));
    }
}
