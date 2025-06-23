package com.studdit.recurringschedule.service;

import com.studdit.recurringschedule.RecurrenceRule;
import com.studdit.recurringschedule.RecurrenceRuleRepository;
import com.studdit.recurringschedule.RecurringScheduleRepository;
import com.studdit.recurringschedule.request.RecurrenceRuleCreateRequest;
import com.studdit.recurringschedule.request.RecurringScheduleCreateRequest;
import com.studdit.recurringschedule.response.RecurringScheduleCreateResponse;
import com.studdit.schedule.enums.RecurrenceType;
import com.studdit.schedule.enums.Visibility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class RecurringSingleScheduleServiceTest {

    @Autowired
    private RecurringScheduleService recurringScheduleService;

    @Autowired
    private RecurrenceRuleRepository recurrenceRuleRepository;

    @Autowired
    private RecurringScheduleRepository recurringScheduleRepository;


    @Test
    @DisplayName("반복 일정을 생성한다")
    void createRecurringScheduleIntegrationTest() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 1, 11, 0);
        LocalDate endDate = LocalDate.of(2024, 1, 3);

        RecurrenceRuleCreateRequest ruleRequest = RecurrenceRuleCreateRequest.builder()
                .recurrenceType(RecurrenceType.DAILY)
                .maxOccurrences(3)
                .endDate(endDate)
                .build();

        RecurringScheduleCreateRequest request = RecurringScheduleCreateRequest.builder()
                .title("매일 회의")
                .description("일일 스탠드업 회의")
                .category("업무")
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .visibility(Visibility.PUBLIC)
                .recurrenceRuleCreateRequest(ruleRequest)
                .build();

        // when
        RecurringScheduleCreateResponse response = recurringScheduleService.createSchedule(request);

        // then
        // 1. Response 검증
        assertThat(response.getSchedules()).isNotNull();
        assertThat(response.getSchedules()).hasSize(3);
        assertThat(response.getRecurrenceRule()).isNotNull();

        // 2. RecurrenceRule 저장 검증
        RecurrenceRule savedRule = recurrenceRuleRepository.findById(response.getRecurrenceRule().getId())
                .orElseThrow(() -> new AssertionError("RecurrenceRule not found"));
        assertThat(savedRule)
                .extracting("recurrenceType", "maxOccurrences", "endDate")
                .contains(RecurrenceType.DAILY, 3, endDate);

        // 3. RecurringSchedule 저장 검증
        assertThat(recurringScheduleRepository.count()).isEqualTo(3);

        // 4. Response DTO 변환 검증
        assertThat(response.getSchedules())
                .extracting("title", "description", "category", "visibility")
                .containsOnly(
                        tuple("매일 회의", "일일 스탠드업 회의", "업무", Visibility.PUBLIC)
                );
    }

}