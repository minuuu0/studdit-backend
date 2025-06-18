package com.studdit.review.service;

import com.studdit.review.Repository.Review;
import com.studdit.review.Repository.ReviewRepository;
import com.studdit.review.request.ReviewCreateServiceRequest;
import com.studdit.review.request.ReviewModifyServiceRequest;
import com.studdit.review.response.ReviewResponse;
import com.studdit.schedule.SingleSchedule;
import com.studdit.schedule.SingleScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private SingleScheduleRepository singleScheduleRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    @DisplayName("회고를 생성한다.")
    void createReview() {
        //given
        ReviewCreateServiceRequest request = ReviewCreateServiceRequest.builder()
                .scheduleId(1L)
                .content("회고 내용")
                .difficulty(3)
                .tags(List.of("알고리즘", "그래프"))
                .build();

        Review review = request.toEntity();

        Review savedReview = Review.builder()
                .id(1L)
                .scheduleId(review.getScheduleId())
                .content(review.getContent())
                .difficulty(review.getDifficulty())
                .tags(review.getTags())
                .build();

        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);

        //when
        ReviewResponse response = reviewService.createReview(request);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getReviewId()).isEqualTo(savedReview.getId());
        assertThat(response.getScheduleId()).isEqualTo(savedReview.getScheduleId());
        assertThat(response.getContent()).isEqualTo(savedReview.getContent());
        assertThat(response.getDifficulty()).isEqualTo(savedReview.getDifficulty());
    }

    @Test
    @DisplayName("스케줄 ID와 회고 ID로 회고를 조회한다.")
    void findReview() {
        // given
        Long scheduleId = 1L;
        Long reviewId = 1L;

        SingleSchedule singleSchedule = SingleSchedule.builder()
                .id(scheduleId)
                .title("일정 제목")
                .build();

        Review review = Review.builder()
                .id(reviewId)
                .scheduleId(scheduleId)
                .content("회고 내용")
                .difficulty(3)
                .tags(List.of("알고리즘", "그래프"))
                .build();

        when(singleScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(singleSchedule));
        when(reviewRepository.findByIdAndScheduleId(reviewId, scheduleId)).thenReturn(Optional.of(review));

        // when
        ReviewResponse response = reviewService.findReview(scheduleId, reviewId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getReviewId()).isEqualTo(reviewId);
        assertThat(response.getScheduleId()).isEqualTo(scheduleId);
        assertThat(response.getContent()).isEqualTo(review.getContent());
        assertThat(response.getDifficulty()).isEqualTo(review.getDifficulty());
        assertThat(response.getTags()).isEqualTo(review.getTags());
    }

    @Test
    @DisplayName("존재하지 않는 스케줄로 회고를 조회하려고 하면 예외가 발생한다.")
    void findReviewWithNonExistentSchedule() {
        // given
        Long scheduleId = 999L;
        Long reviewId = 1L;

        when(singleScheduleRepository.findById(scheduleId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reviewService.findReview(scheduleId, reviewId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("존재하지 않는 회고를 조회하려고 하면 예외가 발생한다.")
    void findNonExistentReview() {
        // given
        Long scheduleId = 1L;
        Long reviewId = 999L;

        SingleSchedule singleSchedule = SingleSchedule.builder()
                .id(scheduleId)
                .title("일정 제목")
                .build();

        when(singleScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(singleSchedule));
        when(reviewRepository.findByIdAndScheduleId(reviewId, scheduleId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reviewService.findReview(scheduleId, reviewId))
                .isInstanceOf(EntityNotFoundException.class);
    }



    @Test
    @DisplayName("회고를 수정한다.")
    void modifyReview() {
        // given
        Long reviewId = 1L;
        Long scheduleId = 1L;

        Review originalReview = Review.builder()
                .id(reviewId)
                .scheduleId(scheduleId)
                .content("기존 회고 내용")
                .difficulty(2)
                .tags(List.of("기존 태그1", "기존 태그2"))
                .build();

        ReviewModifyServiceRequest request = ReviewModifyServiceRequest.builder()
                .reviewId(reviewId)
                .content("수정된 회고 내용")
                .difficulty(4)
                .tags(List.of("수정된 태그1", "수정된 태그2"))
                .build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(originalReview));

        // when
        ReviewResponse response = reviewService.modifyReview(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getReviewId()).isEqualTo(reviewId);
        assertThat(response.getScheduleId()).isEqualTo(scheduleId);
        assertThat(response.getContent()).isEqualTo("수정된 회고 내용");
        assertThat(response.getDifficulty()).isEqualTo(4);
        assertThat(response.getTags()).containsExactly("수정된 태그1", "수정된 태그2");
    }

    @Test
    @DisplayName("존재하지 않는 회고를 수정하려고 하면 예외가 발생한다.")
    void modifyNonExistentReview() {
        // given
        Long reviewId = 999L;

        ReviewModifyServiceRequest request = ReviewModifyServiceRequest.builder()
                .reviewId(reviewId)
                .content("수정된 회고 내용")
                .difficulty(4)
                .tags(List.of("수정된 태그1", "수정된 태그2"))
                .build();

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reviewService.modifyReview(request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("회고를 삭제한다.")
    void deleteReview() {
        // given
        Long scheduleId = 1L;
        Long reviewId = 1L;

        SingleSchedule singleSchedule = SingleSchedule.builder()
                .id(scheduleId)
                .title("일정 제목")
                .build();

        Review review = Review.builder()
                .id(reviewId)
                .scheduleId(scheduleId)
                .content("회고 내용")
                .difficulty(3)
                .tags(List.of("알고리즘", "그래프"))
                .build();

        when(singleScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(singleSchedule));
        when(reviewRepository.findByIdAndScheduleId(reviewId, scheduleId)).thenReturn(Optional.of(review));

        // when
        ReviewResponse response = reviewService.deleteReview(scheduleId, reviewId);

        // then
        assertThat(response).isNull();
        verify(reviewRepository).delete(review);
    }


    @Test
    @DisplayName("존재하지 않는 회고를 삭제하려고 하면 예외가 발생한다.")
    void deleteNonExistentReview() {
        // given
        Long scheduleId = 1L;
        Long reviewId = 999L;

        SingleSchedule singleSchedule = SingleSchedule.builder()
                .id(scheduleId)
                .title("일정 제목")
                .build();

        when(singleScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(singleSchedule));
        when(reviewRepository.findByIdAndScheduleId(reviewId, scheduleId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reviewService.deleteReview(scheduleId, reviewId))
                .isInstanceOf(EntityNotFoundException.class);
    }


}