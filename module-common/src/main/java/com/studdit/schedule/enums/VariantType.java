package com.studdit.schedule.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum VariantType {
    ORIGINAL("원본"),
    MODIFIED("수정됨"),
    CANCELLED("취소됨");

    private final String text;
}
