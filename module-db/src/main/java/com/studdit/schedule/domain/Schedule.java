package com.studdit.schedule.domain;

import com.studdit.BaseEntity;
import com.studdit.schedule.enums.Visibility;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private String category;

    private Boolean isRecurring;


    @Builder
    private Schedule(Long id, String title, String description, String category, Boolean isRecurring) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.isRecurring = isRecurring;
    }

    public void update(Schedule schedule) {
        this.title = schedule.getTitle();
        this.description = schedule.getDescription();
        this.category = schedule.getCategory();
        this.isRecurring = schedule.getIsRecurring();
    }
}
