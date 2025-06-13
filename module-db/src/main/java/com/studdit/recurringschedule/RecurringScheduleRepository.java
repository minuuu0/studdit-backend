package com.studdit.recurringschedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecurringScheduleRepository extends JpaRepository<RecurringSchedule, Long> {
    List<RecurringSchedule> findByRecurrenceRuleId(Long recurrenceRuleId);
    void deleteByRecurrenceRuleId(Long recurrenceRuleId);
}
