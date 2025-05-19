package com.studdit.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.startDateTime BETWEEN :start AND :end")
    List<Schedule> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
