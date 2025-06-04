package com.studdit.schedule.repository;

import com.studdit.schedule.domain.ScheduleInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleInstanceRepsitory extends JpaRepository<ScheduleInstance, Long> {
    @Query("SELECT si FROM ScheduleInstance si WHERE " +
           "(si.startDateTime BETWEEN :start AND :end) OR " +
           "(si.endDateTime BETWEEN :start AND :end) OR " +
           "(si.startDateTime <= :start AND si.endDateTime >= :end)")
    List<ScheduleInstance> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    List<ScheduleInstance> findByScheduleId(Long scheduleId);
}
