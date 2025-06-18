package com.studdit.recurringschedule.service;

import com.studdit.recurringschedule.RecurrenceRule;
import com.studdit.recurringschedule.RecurrenceRuleRepository;
import com.studdit.recurringschedule.RecurringSchedule;
import com.studdit.recurringschedule.RecurringScheduleRepository;
import com.studdit.recurringschedule.request.RecurrenceRuleCreateRequest;
import com.studdit.recurringschedule.request.RecurringScheduleCreateRequest;
import com.studdit.recurringschedule.response.RecurringScheduleCreateResponse;
import com.studdit.schedule.enums.RecurrenceType;
import com.studdit.schedule.enums.Visibility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class RecurringSingleScheduleServiceTest {

    @Autowired
    private RecurringScheduleService recurringScheduleService;

    @Autowired
    private RecurrenceRuleRepository recurrenceRuleRepository;

    @Autowired
    private RecurringScheduleRepository recurringScheduleRepository;


    // 반복규칙, 반복일정 하나의 테스트에서 두 개의 책임을 갖는데 타당할까..?
    @Test
    @DisplayName("반복 일정을 생성한다.")
    void createRecurrenceSchedule() {
        //given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 1, 11, 0);
        LocalDate endDate = LocalDate.of(2024, 1, 5);

        RecurrenceRuleCreateRequest ruleRequest = RecurrenceRuleCreateRequest.builder()
                .recurrenceType(RecurrenceType.DAILY)
                .maxOccurrences(5)
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

        //when
        RecurringScheduleCreateResponse response = recurringScheduleService.createSchedule(request);

        //then
        assertThat(response.getSchedules()).isNotNull();
        assertThat(response.getSchedules()).hasSize(5);

        RecurrenceRule savedRule = recurrenceRuleRepository.findById(response.getRecurrenceRule().getId()).get();
        assertThat(savedRule)
                .extracting("recurrenceType", "maxOccurrences", "endDate")
                .contains(RecurrenceType.DAILY, 5, endDate);

        List<RecurringSchedule> savedSchedules = recurringScheduleRepository.findAll();
        assertThat(savedSchedules).hasSize(5);

        assertThat(savedSchedules)
                .extracting("title", "description", "category", "visibility")
                .containsOnly(
                        tuple("매일 회의", "일일 스탠드업 회의", "업무", Visibility.PUBLIC)
                );
    }

    @DisplayName("최대 반복 횟수와 종료시간이 null인 경우 기본값 1000을 사용한다")
    @Test
    void createScheduleWithMaxOccurrencesAndEndDate() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 1, 11, 0);

        RecurrenceRuleCreateRequest ruleRequest = RecurrenceRuleCreateRequest.builder()
                .recurrenceType(RecurrenceType.DAILY)
                .maxOccurrences(null) // null로 설정
                .endDate(null)
                .build();

        RecurringScheduleCreateRequest request = RecurringScheduleCreateRequest.builder()
                .title("무제한 회의")
                .description("테스트")
                .category("업무")
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .visibility(Visibility.PRIVATE)
                .recurrenceRuleCreateRequest(ruleRequest)
                .build();

        // when
        RecurringScheduleCreateResponse response = recurringScheduleService.createSchedule(request);

        // then
        // endDate로 인해 3개만 생성되어야 함 (1/1, 1/2, 1/3)
        List<RecurringSchedule> savedSchedules = recurringScheduleRepository.findAll();
        assertThat(savedSchedules).hasSize(1000);
    }

    @DisplayName("종료 날짜가 없는 경우 반복 횟수만큼 생성한다")
    @Test
    void createScheduleWithoutEndDate() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 14, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 1, 15, 0);

        RecurrenceRuleCreateRequest ruleRequest = RecurrenceRuleCreateRequest.builder()
                .recurrenceType(RecurrenceType.DAILY)
                .maxOccurrences(3)
                .endDate(null) // 종료 날짜 없음
                .build();

        RecurringScheduleCreateRequest request = RecurringScheduleCreateRequest.builder()
                .title("제한된 회의")
                .description("3회만 진행")
                .category("회의")
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .visibility(Visibility.PUBLIC)
                .recurrenceRuleCreateRequest(ruleRequest)
                .build();

        // when
        RecurringScheduleCreateResponse response = recurringScheduleService.createSchedule(request);

        // then
        List<RecurringSchedule> savedSchedules = recurringScheduleRepository.findAll();
        assertThat(savedSchedules).hasSize(3);
    }

    @DisplayName("반복횟수가 없는 경우 종료날짜만큼 생성한다")
    @Test
    void createScheduleWithoutmaxOccurrences() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 14, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 1, 15, 0);
        LocalDate endDate = LocalDate.of(2024, 1, 5);

        RecurrenceRuleCreateRequest ruleRequest = RecurrenceRuleCreateRequest.builder()
                .recurrenceType(RecurrenceType.DAILY)
                .maxOccurrences(null)
                .endDate(endDate)
                .build();

        RecurringScheduleCreateRequest request = RecurringScheduleCreateRequest.builder()
                .title("제한된 회의")
                .description("3회만 진행")
                .category("회의")
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .visibility(Visibility.PUBLIC)
                .recurrenceRuleCreateRequest(ruleRequest)
                .build();

        // when
        RecurringScheduleCreateResponse response = recurringScheduleService.createSchedule(request);

        // then
        List<RecurringSchedule> savedSchedules = recurringScheduleRepository.findAll();
        assertThat(savedSchedules).hasSize(5);
    }
}