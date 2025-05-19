package com.studdit.review.controller;

import com.studdit.ApiResponse;
import com.studdit.review.request.ReviewCreateRequest;
import com.studdit.review.request.ReviewModifyRequest;
import com.studdit.review.response.ReviewResponse;
import com.studdit.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/schedules/{scheduleId}/review")
    private ApiResponse<ReviewResponse> createReview(@PathVariable Long scheduleId, @RequestBody ReviewCreateRequest request) {
        return ApiResponse.ok(reviewService.createReview(request.toServiceRequest(scheduleId)));
    }

    @GetMapping("/schedules/{scheduleId}/review/{reviewId}")
    private ApiResponse<ReviewResponse> findReview(@PathVariable Long scheduleId, @PathVariable Long reviewId) {
        return ApiResponse.ok(reviewService.findReview(scheduleId, reviewId));
    }

    @PutMapping("/schedules/{scheduleId}/review/{reviewId}")
    private ApiResponse<ReviewResponse> ModifyReview(@PathVariable Long scheduleId, @PathVariable Long reviewId, @RequestBody ReviewModifyRequest request) {
        return ApiResponse.ok(reviewService.modifyReview(request.toServiceRequest(scheduleId, reviewId)));
    }

    @DeleteMapping("/schedules/{scheduleId}/review/{reviewId}")
    private ApiResponse<ReviewResponse> ModifyReview(@PathVariable Long scheduleId, @PathVariable Long reviewId) {
        return ApiResponse.ok(reviewService.deleteReview(scheduleId, reviewId));
    }



}
