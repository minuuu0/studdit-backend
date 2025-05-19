package com.studdit.review.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewModifyRequest {
    Long reviewId;
    private String content;
    private int difficulty;
    private List<String> tags;

    public ReviewModifyServiceRequest toServiceRequest(Long scheduleId, Long reviewId) {
        return ReviewModifyServiceRequest.builder()
                .scheduleId(scheduleId)
                .reviewId(reviewId)
                .content(content)
                .difficulty(difficulty)
                .tags(tags)
                .build();
    }

    @Builder
    private ReviewModifyRequest(Long reviewId, String content, int difficulty, List<String> tags) {
        this.reviewId = reviewId;
        this.content = content;
        this.difficulty = difficulty;
        this.tags = tags;
    }
}
