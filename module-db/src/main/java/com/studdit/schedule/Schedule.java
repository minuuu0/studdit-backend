package com.studdit.schedule;

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

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;


    @Builder
    private Schedule(Long id, String title, String description, String category, LocalDateTime startDateTime, LocalDateTime endDateTime, Visibility visibility) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.visibility = visibility;
    }

    public void update(Schedule schedule) {
        this.title = schedule.getTitle();
        this.description = schedule.getDescription();
        this.category = schedule.getCategory();
        this.startDateTime = schedule.getStartDateTime();
        this.endDateTime = schedule.getEndDateTime();
        this.visibility = schedule.getVisibility();
    }
}
