package com.studdit.schedule.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ScheduleDeleteResponse {

    private Long id;
    private boolean success;
    private String message;

    @Builder
    private ScheduleDeleteResponse(
            Long id,
            boolean success,
            String message
    ) {
        this.id = id;
        this.success = success;
        this.message = message;
    }

    public static ScheduleDeleteResponse of(Long id, boolean success) {
        String message = success ? "일정이 성공적으로 삭제되었습니다." : "일정 삭제에 실패했습니다.";
        return ScheduleDeleteResponse.builder()
                .id(id)
                .success(success)
                .message(message)
                .build();
    }
}