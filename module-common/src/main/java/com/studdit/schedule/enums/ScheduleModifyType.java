package com.studdit.schedule.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ScheduleModifyType {

    THIS_ONLY("현재 일정만 수정"),
    THIS_AND_FUTURE("현재 일정 및 향후 일정 수정"),
    ALL_OCCURRENCES("모든 반복 일정 수정");

    private final String text;
}
