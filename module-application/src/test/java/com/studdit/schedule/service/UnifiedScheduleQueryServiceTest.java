package com.studdit.schedule.service;

import com.studdit.recurringschedule.RecurrenceRule;
import com.studdit.recurringschedule.RecurrenceRuleRepository;
import com.studdit.recurringschedule.RecurringSchedule;
import com.studdit.recurringschedule.RecurringScheduleRepository;
import com.studdit.recurringschedule.request.RecurrenceRuleCreateRequest;
import com.studdit.recurringschedule.request.RecurringScheduleCreateRequest;
import com.studdit.recurringschedule.service.RecurringScheduleService;
import com.studdit.schedule.SingleSchedule;
import com.studdit.schedule.SingleScheduleRepository;
import com.studdit.schedule.enums.RecurrenceType;
import com.studdit.schedule.enums.Visibility;
import com.studdit.schedule.request.ScheduleCreateServiceRequest;
import com.studdit.schedule.response.UnifiedScheduleResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class UnifiedScheduleQueryServiceTest {

    @Autowired
    private UnifiedScheduleQueryService unifiedScheduleQueryService;
    
    @Autowired
    private ScheduleService scheduleService;
    
    @Autowired
    private RecurringScheduleService recurringScheduleService;
    
    @Autowired
    private SingleScheduleRepository singleScheduleRepository;
    
    @Autowired
    private RecurringScheduleRepository recurringScheduleRepository;
    
    @Autowired
    private RecurrenceRuleRepository recurrenceRuleRepository;

    @AfterEach
    void cleanUp() {
        // 테스트 간 데이터 정합성을 위해 모든 데이터 삭제
        recurringScheduleRepository.deleteAll();
        recurrenceRuleRepository.deleteAll();
        singleScheduleRepository.deleteAll();
    }

    @Test
    @DisplayName("단일 일정과 반복 일정을 함께 조회하여 시간 순으로 정렬된다")
    void findUnifiedSchedulesOrderedByTime() {
        // given
        LocalDateTime baseDate = LocalDateTime.of(2024, 6, 15, 0, 0);
        
        // 1. 단일 일정 생성 (6월 15일 14시)
        ScheduleCreateServiceRequest singleRequest = ScheduleCreateServiceRequest.builder()
                .title("단일 회의")
                .description("개별 회의")
                .category("업무")
                .startDateTime(LocalDateTime.of(2024, 6, 15, 14, 0))
                .endDateTime(LocalDateTime.of(2024, 6, 15, 15, 0))
                .visibility(Visibility.PUBLIC)
                .build();
        scheduleService.createSchedule(singleRequest);
        
        // 2. 반복 일정 생성 (6월 15일부터 매일, 10시)
        RecurrenceRuleCreateRequest ruleRequest = RecurrenceRuleCreateRequest.builder()
                .recurrenceType(Recurrenc   eType.DAILY)
                .maxOccurrences(3)
                .endDate(LocalDate.of(2024, 6, 17))
                .build();
        
        RecurringScheduleCreateRequest recurringRequest = RecurringScheduleCreateRequest.builder()
                .title("매일 스탠드업")
                .description("일일 회의")
                .category("업무")
                .startDateTime(LocalDateTime.of(2024, 6, 15, 10, 0))
                .endDateTime(LocalDateTime.of(2024, 6, 15, 11, 0))
                .visibility(Visibility.PUBLIC)
                .recurrenceRuleCreateRequest(ruleRequest)
                .build();
        recurringScheduleService.createSchedule(recurringRequest);
        
        // when
        List<UnifiedScheduleResponse> schedules = unifiedScheduleQueryService.findSchedules(
                "testuser", "day", LocalDateTime.of(2024, 6, 15, 12, 0));
        
        // then
        assertThat(schedules).hasSize(2); // 단일 1개 + 반복 1개 (해당 날짜)
        
        // 시간 순 정렬 확인 (10시 반복 일정 -> 14시 단일 일정)
        assertThat(schedules.get(0).getTitle()).isEqualTo("매일 스탠드업");
        assertThat(schedules.get(0).getScheduleType()).isEqualTo(UnifiedScheduleResponse.ScheduleType.RECURRING);
        assertThat(schedules.get(0).getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 6, 15, 10, 0));
        assertThat(schedules.get(0).getRecurrenceRuleId()).isNotNull();
        
        assertThat(schedules.get(1).getTitle()).isEqualTo("단일 회의");
        assertThat(schedules.get(1).getScheduleType()).isEqualTo(UnifiedScheduleResponse.ScheduleType.SINGLE);
        assertThat(schedules.get(1).getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 6, 15, 14, 0));
        assertThat(schedules.get(1).getRecurrenceRuleId()).isNull();

        
    }
    
    @Test
    @DisplayName("주 단위 조회 시 해당 주의 모든 일정을 조회한다")
    void findSchedulesByWeekView() {
        // given
        LocalDateTime weekStart = LocalDateTime.of(2024, 6, 10, 0, 0); // 월요일
        
        // 1. 주 중간에 단일 일정 (수요일)
        ScheduleCreateServiceRequest singleRequest = ScheduleCreateServiceRequest.builder()
                .title("수요일 회의")
                .description("중간 회의")
                .category("업무")
                .startDateTime(LocalDateTime.of(2024, 6, 12, 14, 0))
                .endDateTime(LocalDateTime.of(2024, 6, 12, 15, 0))
                .visibility(Visibility.PUBLIC)
                .build();
        scheduleService.createSchedule(singleRequest);
        
        // 2. 매일 반복 일정 (월~금)
        RecurrenceRuleCreateRequest ruleRequest = RecurrenceRuleCreateRequest.builder()
                .recurrenceType(RecurrenceType.DAILY)
                .maxOccurrences(5)
                .endDate(LocalDate.of(2024, 6, 14))
                .build();
        
        RecurringScheduleCreateRequest recurringRequest = RecurringScheduleCreateRequest.builder()
                .title("매일 스탠드업")
                .description("일일 회의")
                .category("업무")
                .startDateTime(LocalDateTime.of(2024, 6, 10, 9, 0))
                .endDateTime(LocalDateTime.of(2024, 6, 10, 10, 0))
                .visibility(Visibility.PUBLIC)
                .recurrenceRuleCreateRequest(ruleRequest)
                .build();
        recurringScheduleService.createSchedule(recurringRequest);
        
        // when - 수요일 기준으로 주 단위 조회
        List<UnifiedScheduleResponse> schedules = unifiedScheduleQueryService.findSchedules(
                "testuser", "week", LocalDateTime.of(2024, 6, 12, 12, 0));
        
        // then
        assertThat(schedules).hasSize(6); // 반복 5개 + 단일 1개
        
        // 반복 일정 확인
        long recurringCount = schedules.stream()
                .filter(s -> s.getScheduleType() == UnifiedScheduleResponse.ScheduleType.RECURRING)
                .count();
        assertThat(recurringCount).isEqualTo(5);
        
        // 단일 일정 확인
        long singleCount = schedules.stream()
                .filter(s -> s.getScheduleType() == UnifiedScheduleResponse.ScheduleType.SINGLE)
                .count();
        assertThat(singleCount).isEqualTo(1);
        
        // 시간 순 정렬 확인 (첫 번째는 월요일 9시 반복 일정)
        assertThat(schedules.get(0).getTitle()).isEqualTo("매일 스탠드업");
        assertThat(schedules.get(0).getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 6, 10, 9, 0));
    }
    
    @Test
    @DisplayName("월 단위 조회 시 해당 월의 모든 일정을 조회한다")
    void findSchedulesByMonthView() {
        // given
        // 1. 월 초 단일 일정
        ScheduleCreateServiceRequest earlyRequest = ScheduleCreateServiceRequest.builder()
                .title("월 초 회의")
                .description("월초 회의")
                .category("업무")
                .startDateTime(LocalDateTime.of(2024, 6, 3, 14, 0))
                .endDateTime(LocalDateTime.of(2024, 6, 3, 15, 0))
                .visibility(Visibility.PUBLIC)
                .build();
        scheduleService.createSchedule(earlyRequest);
        
        // 2. 월 말 단일 일정
        ScheduleCreateServiceRequest lateRequest = ScheduleCreateServiceRequest.builder()
                .title("월 말 회의")
                .description("월말 회의")
                .category("업무")
                .startDateTime(LocalDateTime.of(2024, 6, 28, 16, 0))
                .endDateTime(LocalDateTime.of(2024, 6, 28, 17, 0))
                .visibility(Visibility.PUBLIC)
                .build();
        scheduleService.createSchedule(lateRequest);
        
        // 3. 주간 반복 일정 (월요일마다)
        RecurrenceRuleCreateRequest weeklyRule = RecurrenceRuleCreateRequest.builder()
                .recurrenceType(RecurrenceType.WEEKLY)
                .maxOccurrences(4)
                .endDate(LocalDate.of(2024, 6, 30))
                .build();
        
        RecurringScheduleCreateRequest weeklyRequest = RecurringScheduleCreateRequest.builder()
                .title("주간 회의")
                .description("매주 월요일")
                .category("업무")
                .startDateTime(LocalDateTime.of(2024, 6, 3, 10, 0)) // 첫 번째 월요일
                .endDateTime(LocalDateTime.of(2024, 6, 3, 11, 0))
                .visibility(Visibility.PUBLIC)
                .recurrenceRuleCreateRequest(weeklyRule)
                .build();
        recurringScheduleService.createSchedule(weeklyRequest);
        
        // when - 6월 중순 기준으로 월 단위 조회
        List<UnifiedScheduleResponse> schedules = unifiedScheduleQueryService.findSchedules(
                "testuser", "month", LocalDateTime.of(2024, 6, 15, 12, 0));
        
        // then
        assertThat(schedules).hasSize(6); // 단일 2개 + 반복 4개
        
        // 6월 범위 내 일정들 확인
        assertThat(schedules).allMatch(schedule -> 
                schedule.getStartDateTime().getMonthValue() == 6 &&
                schedule.getStartDateTime().getYear() == 2024
        );
        
        // 시간 순 정렬 확인 (가장 이른 일정부터)
        assertThat(schedules.get(0).getStartDateTime())
                .isEqualTo(LocalDateTime.of(2024, 6, 3, 10, 0));
    }
    
    @Test
    @DisplayName("지원하지 않는 뷰 타입으로 조회 시 예외가 발생한다")
    void findSchedulesWithInvalidViewType() {
        // when & then
        assertThatThrownBy(() -> 
                unifiedScheduleQueryService.findSchedules("testuser", "invalid", LocalDateTime.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지원하지 않는 뷰 타입입니다");
    }
    
    @Test
    @DisplayName("일정이 없는 경우 빈 목록을 반환한다")
    void findSchedulesWhenNoSchedulesExist() {
        // when
        List<UnifiedScheduleResponse> schedules = unifiedScheduleQueryService.findSchedules(
                "testuser", "day", LocalDateTime.now());
        
        // then
        assertThat(schedules).isEmpty();
    }
}