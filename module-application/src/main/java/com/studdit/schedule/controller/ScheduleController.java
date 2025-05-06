package com.studdit.schedule.controller;

import com.studdit.ApiResponse;
import com.studdit.schedule.request.ScheduleCreateRequest;
import com.studdit.schedule.response.ScheduleResponse;
import com.studdit.schedule.request.ScheduleModifyRequest;
import com.studdit.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedules")
    private ApiResponse<ScheduleResponse> createSchedule(@RequestBody ScheduleCreateRequest request) {

        return ApiResponse.ok(scheduleService.createSchedule(request.toServiceRequest()));
    }

    @PutMapping("/schedules/{id}")
    private ApiResponse<ScheduleResponse> modifySchedule(@PathVariable Long id, @RequestBody ScheduleModifyRequest request) {
        return ApiResponse.ok(scheduleService.modifySchedule(request.toServiceRequest(id)));
    }
}
