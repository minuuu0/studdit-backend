package com.studdit.schedule.repository;

import com.studdit.schedule.domain.ScheduleInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleInstanceRepsitory extends JpaRepository<ScheduleInstance, Long> {
}
