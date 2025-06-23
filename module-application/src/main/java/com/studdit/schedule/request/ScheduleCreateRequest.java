package com.studdit.schedule.request;

import com.studdit.schedule.enums.Visibility;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScheduleCreateRequest {

    @NotNull
    private String title;

    private String description;

    private String category;

    @NotNull(message = "공유여부는 필수입니다.")
    private Visibility visibility;

    private LocalDateTime startDateTime;     // 시작 일시

    private LocalDateTime endDateTime;       // 종료 일시



    @Builder
    private ScheduleCreateRequest(
            String title,
            String description,
            String category,
            Visibility visibility,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public ScheduleCreateServiceRequest toServiceRequest() {
        return ScheduleCreateServiceRequest.builder()
                .title(title)
                .description(description)
                .category(category)
                .visibility(visibility)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();
    }

}
