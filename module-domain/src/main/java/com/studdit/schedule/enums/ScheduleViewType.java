package com.studdit.schedule.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleViewType {

    DAY("일간"),
    WEEK("주간"),
    MONTH("월간");

    private final String text;
}
