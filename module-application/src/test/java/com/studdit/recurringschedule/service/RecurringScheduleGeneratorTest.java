package com.studdit.recurringschedule.service;

import com.studdit.recurringschedule.RecurrenceRule;
import com.studdit.recurringschedule.RecurringSchedule;
import com.studdit.recurringschedule.request.RecurringScheduleCreateRequest;
import com.studdit.schedule.enums.RecurrenceType;
import com.studdit.schedule.enums.Visibility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RecurringScheduleGeneratorTest {

    private RecurringScheduleGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new RecurringScheduleGenerator();
    }

    @Test
    @DisplayName("매일 반복 일정을 생성한다")
    void createDailyRecurringSchedule() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 1, 11, 0);

        RecurringScheduleCreateRequest request = createRequest(
                "매일 회의", "일일 스탠드업", startDateTime, endDateTime
        );

        RecurrenceRule rule = createRule(RecurrenceType.DAILY, 3, null);

        // when
        List<RecurringSchedule> schedules = generator.createRecurringSchedule(request, rule);

        // then
        assertThat(schedules).hasSize(3);
        
        assertThat(schedules.get(0).getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
        assertThat(schedules.get(1).getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 1, 2, 10, 0));
        assertThat(schedules.get(2).getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 1, 3, 10, 0));
        
        assertThat(schedules.get(0).getEndDateTime()).isEqualTo(LocalDateTime.of(2024, 1, 1, 11, 0));
        assertThat(schedules.get(1).getEndDateTime()).isEqualTo(LocalDateTime.of(2024, 1, 2, 11, 0));
        assertThat(schedules.get(2).getEndDateTime()).isEqualTo(LocalDateTime.of(2024, 1, 3, 11, 0));
    }

    @Test
    @DisplayName("매주 반복 일정을 생성한다")
    void createWeeklyRecurringSchedule() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 14, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 1, 15, 0);

        RecurringScheduleCreateRequest request = createRequest(
                "매주 회의", "주간 회의", startDateTime, endDateTime
        );

        RecurrenceRule rule = createRule(RecurrenceType.WEEKLY, 3, null);

        // when
        List<RecurringSchedule> schedules = generator.createRecurringSchedule(request, rule);

        // then
        assertThat(schedules).hasSize(3);
        
        assertThat(schedules.get(0).getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 1, 1, 14, 0));
        assertThat(schedules.get(1).getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 1, 8, 14, 0));
        assertThat(schedules.get(2).getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 1, 15, 14, 0));
    }

    @Test
    @DisplayName("매월 반복 일정을 생성한다")
    void createMonthlyRecurringSchedule() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 15, 9, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 15, 10, 0);

        RecurringScheduleCreateRequest request = createRequest(
                "매월 회의", "월간 회의", startDateTime, endDateTime
        );

        RecurrenceRule rule = createRule(RecurrenceType.MONTHLY, 3, null);

        // when
        List<RecurringSchedule> schedules = generator.createRecurringSchedule(request, rule);

        // then
        assertThat(schedules).hasSize(3);
        
        assertThat(schedules.get(0).getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 1, 15, 9, 0));
        assertThat(schedules.get(1).getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 2, 15, 9, 0));
        assertThat(schedules.get(2).getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 3, 15, 9, 0));
    }

    @Test
    @DisplayName("매년 반복 일정을 생성한다")
    void createYearlyRecurringSchedule() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 12, 25, 18, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 12, 25, 20, 0);

        RecurringScheduleCreateRequest request = createRequest(
                "연례 파티", "크리스마스 파티", startDateTime, endDateTime
        );

        RecurrenceRule rule = createRule(RecurrenceType.YEARLY, 3, null);

        // when
        List<RecurringSchedule> schedules = generator.createRecurringSchedule(request, rule);

        // then
        assertThat(schedules).hasSize(3);
        
        assertThat(schedules.get(0).getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 12, 25, 18, 0));
        assertThat(schedules.get(1).getStartDateTime()).isEqualTo(LocalDateTime.of(2025, 12, 25, 18, 0));
        assertThat(schedules.get(2).getStartDateTime()).isEqualTo(LocalDateTime.of(2026, 12, 25, 18, 0));
    }

    @Test
    @DisplayName("종료 날짜가 있는 경우 해당 날짜까지만 생성한다")
    void createScheduleWithEndDate() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 1, 11, 0);
        LocalDate endDate = LocalDate.of(2024, 1, 3);

        RecurringScheduleCreateRequest request = createRequest(
                "제한된 회의", "3일까지만", startDateTime, endDateTime
        );

        RecurrenceRule rule = createRule(RecurrenceType.DAILY, 10, endDate);

        // when
        List<RecurringSchedule> schedules = generator.createRecurringSchedule(request, rule);

        // then
        assertThat(schedules).hasSize(3);
        assertThat(schedules.get(2).getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 1, 3, 10, 0));
    }

    @Test
    @DisplayName("maxOccurrences가 null인 경우 종료 일정에 맞춰 일정이 생성된다.")
    void createScheduleWithNullMaxOccurrences() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 1, 11, 0);
        LocalDate endDate = LocalDate.of(2024, 1, 3);

        RecurringScheduleCreateRequest request = createRequest(
                "기본값 테스트", "기본값 사용", startDateTime, endDateTime
        );

        RecurrenceRule rule = createRule(RecurrenceType.DAILY, null, endDate);

        // when
        List<RecurringSchedule> schedules = generator.createRecurringSchedule(request, rule);

        // then
        assertThat(schedules).hasSize(3);
    }

    @Test
    @DisplayName("endDate가 없고 maxOccurrences가 null인 경우 기본값 1000개를 생성한다")
    void createScheduleWithNullMaxOccurrencesAndNoEndDate() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 1, 11, 0);

        RecurringScheduleCreateRequest request = createRequest(
                "기본값 테스트", "기본값 사용", startDateTime, endDateTime
        );

        RecurrenceRule rule = createRule(RecurrenceType.DAILY, null, null);

        // when
        List<RecurringSchedule> schedules = generator.createRecurringSchedule(request, rule);

        // then
        assertThat(schedules).hasSize(1000);
    }

    @Test
    @DisplayName("endDate가 startDate 이전인 경우 예외가 발생한다")
    void createScheduleWithEndDateBeforeStartDate() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 5, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 5, 11, 0);
        LocalDate endDate = LocalDate.of(2024, 1, 3);

        RecurringScheduleCreateRequest request = createRequest(
                "잘못된 날짜", "종료일이 시작일보다 빠름", startDateTime, endDateTime
        );

        RecurrenceRule rule = createRule(RecurrenceType.DAILY, 10, endDate);

        // when & then
        assertThatThrownBy(() -> generator.createRecurringSchedule(request, rule))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("반복 종료일은 시작일보다 이후여야 합니다");
    }

    @Test
    @DisplayName("생성된 스케줄의 모든 필드가 올바르게 설정된다")
    void createScheduleWithAllFields() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 1, 11, 0);

        RecurringScheduleCreateRequest request = createRequest(
                "테스트 제목", "테스트 설명", startDateTime, endDateTime
        );

        RecurrenceRule rule = createRule(RecurrenceType.DAILY, 1, null);

        // when
        List<RecurringSchedule> schedules = generator.createRecurringSchedule(request, rule);

        // then
        assertThat(schedules).hasSize(1);
        RecurringSchedule schedule = schedules.get(0);
        
        assertThat(schedule.getTitle()).isEqualTo("테스트 제목");
        assertThat(schedule.getDescription()).isEqualTo("테스트 설명");
        assertThat(schedule.getCategory()).isEqualTo("테스트");
        assertThat(schedule.getVisibility()).isEqualTo(Visibility.PUBLIC);
        assertThat(schedule.getRecurrenceRuleId()).isEqualTo(1L);
    }

    private RecurringScheduleCreateRequest createRequest(String title, String description, 
                                                        LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return RecurringScheduleCreateRequest.builder()
                .title(title)
                .description(description)
                .category("테스트")
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .visibility(Visibility.PUBLIC)
                .build();
    }

    private RecurrenceRule createRule(RecurrenceType type, Integer maxOccurrences, LocalDate endDate) {
        return RecurrenceRule.builder()
                .id(1L)
                .recurrenceType(type)
                .maxOccurrences(maxOccurrences)
                .endDate(endDate)
                .build();
    }
}