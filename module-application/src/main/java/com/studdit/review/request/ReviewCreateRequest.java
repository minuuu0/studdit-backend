package com.studdit.review.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewCreateRequest {
    private String content;
    private int difficulty;
    private List<String> tags;

    @Builder
    private ReviewCreateRequest(String content, int difficulty, List<String> tags) {
        this.content = content;
        this.difficulty = difficulty;
        this.tags = tags;
    }

    public ReviewCreateServiceRequest toServiceRequest(Long scheduleId) {
        return ReviewCreateServiceRequest.builder()
                .scheduleId(scheduleId)
                .content(content)
                .difficulty(difficulty)
                .tags(tags)
                .build();
    }

}
