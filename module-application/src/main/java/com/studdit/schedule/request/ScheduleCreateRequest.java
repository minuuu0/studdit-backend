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

    @NotNull(message = "시작 일시는 필수입니다.")
    private LocalDateTime startDateTime;

    @NotNull(message = "종료 일시는 필수입니다.")
    private LocalDateTime endDateTime;

    @NotNull(message = "공유여부는 필수입니다.")
    private Visibility visibility;

    @Builder
    private ScheduleCreateRequest(
            String title,
            String description,
            String category,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Visibility visibility
    ) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.visibility = visibility;
    }

    public ScheduleCreateServiceRequest toServiceRequest() {
        return ScheduleCreateServiceRequest.builder()
                .title(title)
                .description(description)
                .category(category)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .visibility(visibility)
                .build();
    }
}
