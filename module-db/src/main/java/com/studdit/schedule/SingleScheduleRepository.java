package com.studdit.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SingleScheduleRepository extends JpaRepository<SingleSchedule, Long> {


    @Query("SELECT s FROM SingleSchedule s WHERE " +
            "s.startDateTime <= :end AND s.endDateTime >= :start")
    List<SingleSchedule> findByDateRange(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
