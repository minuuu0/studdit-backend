package com.studdit.schedule.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DayofWeek {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");
    private final String text;
}
