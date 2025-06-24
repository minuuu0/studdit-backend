package com.studdit.recurringschedule;

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
public class RecurringSchedule extends BaseEntity {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private Long recurrenceRuleId;

  private String title;

  @Column(length = 1000)
  private String description;

  private String category;

  private LocalDateTime startDateTime;

  private LocalDateTime endDateTime;

  @Enumerated(EnumType.STRING)
  private Visibility visibility;

  @Builder
  private RecurringSchedule(
          Long id,
          Long recurrenceRuleId,
          String title,
          String description,
          String category,
          LocalDateTime startDateTime,
          LocalDateTime endDateTime,
          Visibility visibility
  ) {
    this.id = id;
    this.recurrenceRuleId = recurrenceRuleId;
    this.title = title;
    this.description = description;
    this.category = category;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.visibility = visibility;
  }

  public void update(String title, String description, String category, 
                    LocalDateTime startDateTime, LocalDateTime endDateTime, 
                    Visibility visibility) {
    this.title = title;
    this.description = description;
    this.category = category;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.visibility = visibility;
  }
  
  public void updateRecurrenceRuleId(Long recurrenceRuleId) {
    this.recurrenceRuleId = recurrenceRuleId;
  }

}
