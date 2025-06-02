package com.studdit.schedule.repository;

import com.studdit.schedule.domain.RecurrenceRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecurrenceRuleRepository extends JpaRepository<RecurrenceRule, Long> {
}
