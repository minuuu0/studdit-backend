package com.studdit.schedule.response;

import com.studdit.schedule.domain.ScheduleInstance;
import com.studdit.schedule.enums.Visibility;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScheduleInstanceResponse {

    Long id;
    Long scheduleId;
    Visibility visibility;
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;
    private boolean reviewWritten;
    private boolean verification;

    @Builder
    private ScheduleInstanceResponse(
            Long id,
            Long scheduleId,
            Visibility visibility,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            boolean reviewWritten,
            boolean verification
    ) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.visibility = visibility;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.reviewWritten = reviewWritten;
        this.verification = verification;
    }

    public static ScheduleInstanceResponse of(ScheduleInstance scheduleInstance) {
        return ScheduleInstanceResponse.builder()
                .id(scheduleInstance.getId())
                .scheduleId(scheduleInstance.getScheduleId())
                .visibility(scheduleInstance.getVisibility())
                .startDateTime(scheduleInstance.getStartDateTime())
                .endDateTime(scheduleInstance.getEndDateTime())
                .reviewWritten(false)
                .verification(false)
                .build();
    }

}
