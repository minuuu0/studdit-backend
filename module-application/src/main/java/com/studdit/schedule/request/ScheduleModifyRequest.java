package com.studdit.schedule.request;

import com.studdit.schedule.enums.ScheduleModifyType;
import com.studdit.schedule.enums.Visibility;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScheduleModifyRequest {

    private Long id;

    private String title;

    private String description;

    private String category;

    @NotNull(message = "공유여부는 필수입니다.")
    private Visibility visibility;

    @NotNull(message = "시작 일시는 필수입니다.")
    private LocalDateTime startDateTime;

    @NotNull(message = "종료 일시는 필수입니다.")
    private LocalDateTime endDateTime;

    private RecurrenceRuleCreateRequest recurrenceRuleCreateRequest;

    private ScheduleModifyType modifyType;

    @Builder
    private ScheduleModifyRequest(
            Long id,
            String title,
            String description,
            String category,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Visibility visibility,
            RecurrenceRuleCreateRequest recurrenceRuleCreateRequest,
            ScheduleModifyType modifyType
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.visibility = visibility;
        this.recurrenceRuleCreateRequest = recurrenceRuleCreateRequest;
        this.modifyType = modifyType;
    }

    public ScheduleModifyServiceRequest toServiceRequest(Long scheduleId, Long instanceId) {
        return ScheduleModifyServiceRequest.builder()
                .scheduleId(scheduleId)
                .instanceId(instanceId)
                .title(title)
                .description(description)
                .category(category)
                .visibility(visibility)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .recurrenceRuleCreateServiceRequest(
                        recurrenceRuleCreateRequest != null ?
                                recurrenceRuleCreateRequest.toServiceRequest() : null
                )
                .modifyType(modifyType)
                .build();
    }

}
