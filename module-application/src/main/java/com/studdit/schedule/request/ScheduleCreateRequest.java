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

    private RecurrenceRuleCreateRequest recurrenceRuleCreateRequest;

    @Builder
    private ScheduleCreateRequest(
            String title,
            String description,
            String category,
            Visibility visibility,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            RecurrenceRuleCreateRequest recurrenceRuleCreateRequest
    ) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.visibility = visibility;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.recurrenceRuleCreateRequest = recurrenceRuleCreateRequest;
    }

    public ScheduleCreateServiceRequest toServiceRequest() {
        return ScheduleCreateServiceRequest.builder()
                .title(title)
                .description(description)
                .category(category)
                .visibility(visibility)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .recurrenceRuleCreateServiceRequest( // to-do 설계에 대한 고민... 현재 null을 확인하는데 이 객체가 단일 원칙을 지킬 수 있는가..? 단일일정과 반복일정에 대한 DTO를 분리해야하나?
                        recurrenceRuleCreateRequest != null ?
                                recurrenceRuleCreateRequest.toServiceRequest() : null

                )
                .build();
    }

}
