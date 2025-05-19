package com.studdit.review.request;

import com.studdit.review.Repository.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewModifyServiceRequest {

    private Long reviewId;
    private Long scheduleId;
    private String content;
    private int difficulty;
    private List<String> tags;

    @Builder
    private ReviewModifyServiceRequest(Long reviewId, Long scheduleId, String content, int difficulty, List<String> tags) {
        this.reviewId = reviewId;
        this.scheduleId = scheduleId;
        this.content = content;
        this.difficulty = difficulty;
        this.tags = tags;
    }

    public Review toEntity() {
        return Review.builder()
                .content(content)
                .difficulty(difficulty)
                .tags(tags)
                .build();
    }
}
