package com.studdit.review.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewCreateServiceRequest {
    private Long scheduleId;
    private String content;
    private int difficulty;
    private List<String> tags;

    @Builder
    private ReviewCreateServiceRequest(Long scheduleId, String content, int difficulty, List<String> tags) {
        this.scheduleId = scheduleId;
        this.content = content;
        this.difficulty = difficulty;
        this.tags = tags;
    }
}
