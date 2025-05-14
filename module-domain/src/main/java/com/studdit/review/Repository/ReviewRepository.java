package com.studdit.review.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByIdAndScheduleId(Long id, Long scheduleId);
}
