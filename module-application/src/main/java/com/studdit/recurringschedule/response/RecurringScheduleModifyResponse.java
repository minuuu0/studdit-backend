package com.studdit.recurringschedule.response;

import com.studdit.recurringschedule.RecurringSchedule;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RecurringScheduleModifyResponse {

    private int modifiedCount;
    private List<RecurringScheduleResponse> updatedSchedules;

    @Builder
    private RecurringScheduleModifyResponse(int modifiedCount, List<RecurringScheduleResponse> updatedSchedules) {
        this.modifiedCount = modifiedCount;
        this.updatedSchedules = updatedSchedules;
    }

    public static RecurringScheduleModifyResponse of(List<RecurringSchedule> updatedSchedules) {
        return RecurringScheduleModifyResponse.builder()
                .modifiedCount(updatedSchedules.size())
                .updatedSchedules(updatedSchedules.stream()
                        .map(RecurringScheduleResponse::of)
                        .toList())
                .build();
    }
}