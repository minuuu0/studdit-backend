package com.studdit.review.service;

import com.studdit.review.Repository.Review;
import com.studdit.review.Repository.ReviewRepository;
import com.studdit.review.request.ReviewCreateServiceRequest;
import com.studdit.review.request.ReviewModifyServiceRequest;
import com.studdit.review.response.ReviewResponse;
import com.studdit.schedule.SingleSchedule;
import com.studdit.schedule.SingleScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SingleScheduleRepository singleScheduleRepository;

    @Transactional
    public ReviewResponse createReview(ReviewCreateServiceRequest request) {

        Review review = request.toEntity();
        Review savedReview = reviewRepository.save(review);
        return ReviewResponse.of(savedReview);
    }

    public ReviewResponse findReview(Long scheduleId, Long reviewId) {

        SingleSchedule singleSchedule = singleScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 일정을 찾을 수 없습니다."));

        Review review = reviewRepository.findByIdAndScheduleId(reviewId, scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("회고를 찾을 수 없습니다"));

        return ReviewResponse.of(review);
    }

    @Transactional
    public ReviewResponse modifyReview(ReviewModifyServiceRequest request) {
        Review requestReview = request.toEntity();
        Review review = reviewRepository.findById(request.getReviewId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 회고를 찾을 수 없습니다."));
        review.update(requestReview);
        return ReviewResponse.of(review);
    }

    @Transactional
    public ReviewResponse deleteReview(Long scheduleId, Long reviewId) {
        SingleSchedule singleSchedule = singleScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 일정을 찾을 수 없습니다."));

        Review review = reviewRepository.findByIdAndScheduleId(reviewId, scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("회고를 찾을 수 없습니다"));

        reviewRepository.delete(review);
        return null;
    }
}
