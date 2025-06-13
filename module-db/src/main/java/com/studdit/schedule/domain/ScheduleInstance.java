package com.studdit.schedule.domain;

import com.studdit.BaseEntity;
import com.studdit.schedule.enums.Visibility;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @Builder
    private ScheduleInstance(Long id, Long scheduleId, LocalDateTime startDateTime, LocalDateTime endDateTime, Visibility visibility) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.visibility = visibility;
    }

    public void update(ScheduleInstance scheduleInstance) {
        this.startDateTime = scheduleInstance.getStartDateTime();
        this.endDateTime = scheduleInstance.getEndDateTime();
        this.visibility = scheduleInstance.getVisibility();
    }

}
