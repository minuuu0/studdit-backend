package com.studdit.recurringschedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecurringScheduleRepository extends JpaRepository<RecurringSchedule, Long> {
    List<RecurringSchedule> findByRecurrenceRuleId(Long recurrenceRuleId);
    void deleteByRecurrenceRuleId(Long recurrenceRuleId);
    
    @Query("SELECT r FROM RecurringSchedule r WHERE r.recurrenceRuleId = :recurrenceRuleId AND r.startDateTime >= :fromDateTime")
    List<RecurringSchedule> findByRecurrenceRuleIdAndStartDateTimeAfter(@Param("recurrenceRuleId") Long recurrenceRuleId, @Param("fromDateTime") LocalDateTime fromDateTime);
    
    @Query("SELECT r FROM RecurringSchedule r WHERE r.startDateTime <= :end AND r.endDateTime >= :start")
    List<RecurringSchedule> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
