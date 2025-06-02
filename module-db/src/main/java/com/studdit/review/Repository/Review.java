package com.studdit.review.Repository;

import com.studdit.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.GenerationType.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long scheduleId;

    @Column(length = 3000)
    private String content;

    private int difficulty;

    @ElementCollection
    private List<String> tags;

    @Builder
    private Review(Long id, Long scheduleId, String content, int difficulty, List<String> tags) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.content = content;
        this.difficulty = difficulty;
        this.tags = tags;
    }

    public void update(Review request) {
        this.content = request.getContent();
        this.difficulty = request.getDifficulty();
        this.tags = request.getTags();
    }
}
