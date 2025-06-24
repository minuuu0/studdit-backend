package com.studdit.recurringschedule;

import com.studdit.BaseEntity;
import com.studdit.schedule.enums.RecurrenceType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class RecurrenceRule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RecurrenceType recurrenceType;

    private Integer maxOccurrences;

    private LocalDate endDate;

    @Builder
    private RecurrenceRule(
            Long id,
            RecurrenceType recurrenceType,
            Integer maxOccurrences,
            LocalDate endDate
    ) {
        this.id = id;
        this.recurrenceType = recurrenceType;
        this.maxOccurrences = maxOccurrences;
        this.endDate = endDate;
    }

    public void update(RecurrenceType recurrenceType, Integer maxOccurrences, LocalDate endDate) {
        this.recurrenceType = recurrenceType;
        this.maxOccurrences = maxOccurrences;
        this.endDate = endDate;
    }
}
