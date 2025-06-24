package com.studdit.recurringschedule.controller;

import com.studdit.ApiResponse;
import com.studdit.recurringschedule.request.RecurringScheduleCreateRequest;
import com.studdit.recurringschedule.request.RecurringScheduleModifyRequest;
import com.studdit.recurringschedule.response.RecurringScheduleCreateResponse;
import com.studdit.recurringschedule.response.RecurringScheduleModifyResponse;
import com.studdit.recurringschedule.service.RecurringScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/schedules")
@RestController
@RequiredArgsConstructor
public class RecurringScheduleController {

    private final RecurringScheduleService recurringScheduleService;


    @PostMapping("/recurring")
    public ApiResponse<RecurringScheduleCreateResponse> createRecurringSchedule(
            @RequestBody RecurringScheduleCreateRequest request
    ) {
        return ApiResponse.ok(recurringScheduleService.createSchedule(request));
    }

    @PutMapping("/recurring/{scheduleId}")
    public ApiResponse<RecurringScheduleModifyResponse> updateRecurringSchedule(
            @PathVariable Long scheduleId,
            @RequestBody RecurringScheduleModifyRequest request
    ) {
        return ApiResponse.ok(recurringScheduleService.modifySchedule(scheduleId, request));
    }
}