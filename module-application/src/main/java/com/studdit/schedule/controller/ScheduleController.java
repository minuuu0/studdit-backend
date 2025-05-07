package com.studdit.schedule.controller;

import com.studdit.ApiResponse;
import com.studdit.schedule.request.ScheduleCreateRequest;
import com.studdit.schedule.response.ScheduleResponse;
import com.studdit.schedule.request.ScheduleModifyRequest;
import com.studdit.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedules")
    private ApiResponse<ScheduleResponse> createSchedule(@Valid @RequestBody ScheduleCreateRequest request) {

        return ApiResponse.ok(scheduleService.createSchedule(request.toServiceRequest()));
    }

    @GetMapping("/schedules")
    private ApiResponse<List<ScheduleResponse>> findSchedules(
            @RequestParam String username,
            @RequestParam(required = false, defaultValue = "month") String view,
            @RequestParam(required = false) LocalDate date
    ) {
        return ApiResponse.ok(scheduleService.findSchedules(username, view, date));
    }

    @PutMapping("/schedules/{id}")
    private ApiResponse<ScheduleResponse> modifySchedule(@Valid @PathVariable Long id, @RequestBody ScheduleModifyRequest request) {
        return ApiResponse.ok(scheduleService.modifySchedule(request.toServiceRequest(id)));
    }
}
