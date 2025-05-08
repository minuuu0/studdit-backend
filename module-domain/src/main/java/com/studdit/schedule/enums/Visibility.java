package com.studdit.schedule.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Visibility {
    PUBLIC("공개"),
    PRIVATE("비공개");

    private final String text;
}
