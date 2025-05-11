package com.studdit.review.response;

import com.studdit.review.request.ReviewCreateServiceRequest;
import com.studdit.review.request.ReviewModifyServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewResponse {
    private Long scheduleId;
    private Long reviewId;
    private String content;
    private int difficulty;
    private List<String> tags;

    @Builder
    private ReviewResponse(Long scheduleId, Long reviewId, String content, int difficulty, List<String> tags) {
        this.scheduleId = scheduleId;
        this.reviewId = reviewId;
        this.content = content;
        this.difficulty = difficulty;
        this.tags = tags;
    }

    public static ReviewResponse of(ReviewCreateServiceRequest serviceRequest) {
        return ReviewResponse.builder()
                .scheduleId(serviceRequest.getScheduleId())
                .reviewId(1L)
                .content(serviceRequest.getContent())
                .difficulty(serviceRequest.getDifficulty())
                .tags(serviceRequest.getTags())
                .build();
    }

    public static ReviewResponse of(ReviewModifyServiceRequest serviceRequest) {
        return ReviewResponse.builder()
                .scheduleId(serviceRequest.getScheduleId())
                .reviewId(serviceRequest.getReviewId())
                .content(serviceRequest.getContent())
                .difficulty(serviceRequest.getDifficulty())
                .tags(serviceRequest.getTags())
                .build();

    }
}
