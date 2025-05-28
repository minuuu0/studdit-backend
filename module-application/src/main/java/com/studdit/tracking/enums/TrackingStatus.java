package com.studdit.tracking.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TrackingStatus {
    IN_PROGRESS("진행 중"),
    PAUSED("일시정지"),
    COMPLETED("완료");

    private final String text;
}
