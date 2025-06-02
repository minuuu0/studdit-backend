package com.studdit.schedule.controller;

import com.studdit.ApiResponse;
import com.studdit.schedule.request.ScheduleCreateRequest;
import com.studdit.schedule.response.ScheduleCreateResponse;
import com.studdit.schedule.request.ScheduleModifyRequest;
import com.studdit.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedules")
    private ApiResponse<ScheduleCreateResponse> createSchedule(@Valid @RequestBody ScheduleCreateRequest request) {

        return ApiResponse.ok(scheduleService.createSchedule(request.toServiceRequest()));
    }

    @GetMapping("/schedules")
    private ApiResponse<List<ScheduleCreateResponse>> findSchedules(
            @RequestParam String username,
            @RequestParam(required = false, defaultValue = "month") String view,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date
    ) {
        return ApiResponse.ok(scheduleService.findSchedules(username, view, date)); // to-do
    }

    @PutMapping("/schedules/{id}")
    private ApiResponse<ScheduleCreateResponse> modifySchedule(@Valid @PathVariable Long id, @RequestBody ScheduleModifyRequest request) {
        return ApiResponse.ok(scheduleService.modifySchedule(request.toServiceRequest(id)));
    }

    @DeleteMapping("/schedules/{id}")
    private ApiResponse<ScheduleCreateResponse> deleteSchedule(@Valid @PathVariable Long id) {
        return ApiResponse.ok(scheduleService.deleteSchedule(id));
    }
}
