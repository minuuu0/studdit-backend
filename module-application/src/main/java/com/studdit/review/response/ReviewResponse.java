package com.studdit.review.response;

import com.studdit.review.Repository.Review;
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

    public static ReviewResponse of(Review review) {
        return ReviewResponse.builder()
                .scheduleId(review.getScheduleId())
                .reviewId(1L)
                .content(review.getContent())
                .difficulty(review.getDifficulty())
                .tags(review.getTags())
                .build();
    }
}
