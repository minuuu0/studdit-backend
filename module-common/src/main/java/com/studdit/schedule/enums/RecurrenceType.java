package com.studdit.schedule.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RecurrenceType {
    DAILY("매일"),
    WEEKLY("매주"),
    MONTHLY("매월"),
    YEARLY("매년");

    private final String text;
}
