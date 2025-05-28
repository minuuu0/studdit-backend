package com.studdit.schedule.domain;

import com.studdit.BaseEntity;
import com.studdit.schedule.enums.VariantType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class ScheduleInstance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long scheduleId;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private Boolean isVariant = false;

    private VariantType variantType;

    @Builder
    private ScheduleInstance(Long id, Long scheduleId, LocalDateTime startDateTime, LocalDateTime endDateTime, Boolean isVariant, VariantType variantType) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.isVariant = isVariant;
        this.variantType = variantType;
    }
}
