package com.studdit.review.service;

import com.studdit.review.request.ReviewCreateServiceRequest;
import com.studdit.review.request.ReviewModifyServiceRequest;
import com.studdit.review.response.ReviewResponse;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    // to-do 추후 수행
    public ReviewResponse createReview(ReviewCreateServiceRequest serviceRequest) {

        // entity 반환
        return ReviewResponse.of(serviceRequest);
    }

    public ReviewResponse findReview(Long scheduleId, Long reviewId) {

        return ReviewResponse.builder()
                .scheduleId(scheduleId)
                .reviewId(reviewId)
                .build();
    }

    public ReviewResponse modifyReview(ReviewModifyServiceRequest serviceRequest) {
        return ReviewResponse.of(serviceRequest);
    }

    public ReviewResponse deleteReview(Long scheduleId, Long reviewId) {
        // delete
        return null;
    }
}
