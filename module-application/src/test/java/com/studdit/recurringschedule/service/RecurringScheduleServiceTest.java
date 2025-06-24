package com.studdit.recurringschedule.service;

import com.studdit.recurringschedule.RecurrenceRule;
import com.studdit.recurringschedule.RecurrenceRuleRepository;
import com.studdit.recurringschedule.RecurringSchedule;
import com.studdit.recurringschedule.RecurringScheduleRepository;
import com.studdit.recurringschedule.request.RecurrenceRuleCreateRequest;
import com.studdit.recurringschedule.request.RecurrenceRuleModifyRequest;
import com.studdit.recurringschedule.request.RecurringScheduleCreateRequest;
import com.studdit.recurringschedule.request.RecurringScheduleModifyRequest;
import com.studdit.recurringschedule.response.RecurringScheduleCreateResponse;
import com.studdit.recurringschedule.response.RecurringScheduleModifyResponse;
import com.studdit.schedule.enums.RecurrenceEditType;
import com.studdit.schedule.enums.RecurrenceType;
import com.studdit.schedule.enums.Visibility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class RecurringScheduleServiceTest {

    @Autowired
    private RecurringScheduleService recurringScheduleService;

    @Autowired
    private RecurrenceRuleRepository recurrenceRuleRepository;

    @Autowired
    private RecurringScheduleRepository recurringScheduleRepository;

    @AfterEach
    void cleanUp() {
        // 테스트 간 데이터 정합성을 위해 모든 데이터 삭제
        recurringScheduleRepository.deleteAll();
        recurrenceRuleRepository.deleteAll();
    }

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

    @Test
    @DisplayName("THIS_ONLY: 반복 규칙 변경을 시도하면 예외가 발생한다")
    void modifyThisOnly_WithRecurrenceRuleChange_ShouldThrowException() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 1, 11, 0);

        // 반복 일정 생성
        RecurringScheduleCreateResponse createResponse = createDailyRecurringSchedule(
                "매일 회의", startDateTime, endDateTime, 5);
        
        Long scheduleId = createResponse.getSchedules().get(0).getId();

        RecurrenceRuleModifyRequest ruleRequest = RecurrenceRuleModifyRequest.builder()
                .recurrenceType(RecurrenceType.WEEKLY)
                .maxOccurrences(10)
                .endDate(LocalDate.of(2024, 2, 1))
                .build();

        RecurringScheduleModifyRequest request = RecurringScheduleModifyRequest.builder()
                .title("수정된 제목")
                .description("수정된 설명")
                .category("수정된 카테고리")
                .startDateTime(startDateTime.plusHours(1))
                .endDateTime(endDateTime.plusHours(1))
                .visibility(Visibility.PRIVATE)
                .editType(RecurrenceEditType.THIS_ONLY)
                .recurrenceRuleModifyRequest(ruleRequest) // 반복 규칙 변경 시도
                .build();

        // when & then
        assertThatThrownBy(() -> recurringScheduleService.modifySchedule(scheduleId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("개별 일정에는 반복 규칙을 변경할 수 없습니다");
    }

    @Test
    @DisplayName("THIS_ONLY: 반복 규칙은 유지하고 제목·시간등의 속성만 수정하면 해당 일정만 변경된다")
    void modifyThisOnly_WithoutRecurrenceRuleChange_ShouldUpdateOnlyTargetSchedule() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 1, 11, 0);

        // 반복 일정 생성 (5개)
        RecurringScheduleCreateResponse createResponse = createDailyRecurringSchedule(
                "매일 회의", startDateTime, endDateTime, 5);
        
        Long ruleId = createResponse.getRecurrenceRule().getId();
        Long targetScheduleId = createResponse.getSchedules().get(2).getId(); // 3번째 일정 선택

        RecurringScheduleModifyRequest request = RecurringScheduleModifyRequest.builder()
                .title("수정된 제목")
                .description("수정된 설명")
                .category("수정된 카테고리")
                .startDateTime(LocalDateTime.of(2024, 1, 3, 14, 0)) // 시간 변경
                .endDateTime(LocalDateTime.of(2024, 1, 3, 15, 0))
                .visibility(Visibility.PRIVATE)
                .editType(RecurrenceEditType.THIS_ONLY)
                .recurrenceRuleModifyRequest(null) // 반복 규칙 변경 없음
                .build();

        // when
        RecurringScheduleModifyResponse response = recurringScheduleService.modifySchedule(targetScheduleId, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getModifiedCount()).isEqualTo(1);
        assertThat(response.getUpdatedSchedules()).hasSize(1);

        // 1. 수정된 일정 확인
        RecurringSchedule modifiedSchedule = recurringScheduleRepository.findById(targetScheduleId).orElseThrow();
        assertThat(modifiedSchedule.getTitle()).isEqualTo("수정된 제목");
        assertThat(modifiedSchedule.getDescription()).isEqualTo("수정된 설명");
        assertThat(modifiedSchedule.getCategory()).isEqualTo("수정된 카테고리");
        assertThat(modifiedSchedule.getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 1, 3, 14, 0));
        assertThat(modifiedSchedule.getEndDateTime()).isEqualTo(LocalDateTime.of(2024, 1, 3, 15, 0));
        assertThat(modifiedSchedule.getVisibility()).isEqualTo(Visibility.PRIVATE);
        // 반복 규칙 ID는 그대로 유지되어야 함
        assertThat(modifiedSchedule.getRecurrenceRuleId()).isEqualTo(ruleId);

        // 2. 다른 일정들은 변경되지 않았는지 확인
        java.util.List<RecurringSchedule> allSchedules = recurringScheduleRepository.findByRecurrenceRuleId(ruleId);
        assertThat(allSchedules).hasSize(5); // 여전히 5개

        long unchangedCount = allSchedules.stream()
                .filter(s -> !s.getId().equals(targetScheduleId))
                .filter(s -> s.getTitle().equals("매일 회의")) // 원래 제목 유지
                .count();
        assertThat(unchangedCount).isEqualTo(4); // 나머지 4개는 변경되지 않음

        // 3. 반복 규칙도 변경되지 않았는지 확인
        RecurrenceRule rule = recurrenceRuleRepository.findById(ruleId).orElseThrow();
        assertThat(rule.getRecurrenceType()).isEqualTo(RecurrenceType.DAILY);
        assertThat(rule.getMaxOccurrences()).isEqualTo(5);
    }

    @Test
    @DisplayName("THIS_AND_FOLLOWING: 반복 규칙을 변경하면 기존 일정들과 수정된 일정들이 서로 다른 규칙을 갖는다")
    void modifyThisAndFollowing_WithRecurrenceRuleChange_ShouldHaveDifferentRules() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 1, 11, 0);

        // 반복 일정 생성 (5개: 1/1, 1/2, 1/3, 1/4, 1/5)
        RecurringScheduleCreateResponse createResponse = createDailyRecurringSchedule(
                "매일 회의", startDateTime, endDateTime, 5);
        
        Long originalRuleId = createResponse.getRecurrenceRule().getId();
        Long targetScheduleId = createResponse.getSchedules().get(2).getId(); // 3번째 일정(1/3) 선택

        RecurrenceRuleModifyRequest ruleRequest = RecurrenceRuleModifyRequest.builder()
                .recurrenceType(RecurrenceType.WEEKLY) // DAILY -> WEEKLY 변경
                .maxOccurrences(3)
                .endDate(LocalDate.of(2024, 2, 1))
                .build();

        RecurringScheduleModifyRequest request = RecurringScheduleModifyRequest.builder()
                .title("수정된 제목")
                .description("수정된 설명")
                .category("수정된 카테고리")
                .startDateTime(LocalDateTime.of(2024, 1, 3, 14, 0))
                .endDateTime(LocalDateTime.of(2024, 1, 3, 15, 0))
                .visibility(Visibility.PRIVATE)
                .editType(RecurrenceEditType.THIS_AND_FOLLOWING)
                .recurrenceRuleModifyRequest(ruleRequest)
                .build();

        // when
        RecurringScheduleModifyResponse response = recurringScheduleService.modifySchedule(targetScheduleId, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getModifiedCount()).isEqualTo(3); // 3번째부터 5번째까지 3개

        // 1. 새로운 반복 규칙이 생성되었는지 확인
        assertThat(recurrenceRuleRepository.count()).isEqualTo(2); // 기존 + 새로운
        
        // 2. 수정된 일정들이 새 반복 규칙에 연결되었는지 확인
        RecurringSchedule modifiedSchedule = recurringScheduleRepository.findById(targetScheduleId).orElseThrow();
        Long newRuleId = modifiedSchedule.getRecurrenceRuleId();
        
        assertThat(newRuleId).isNotEqualTo(originalRuleId); // 새로운 규칙 ID
        assertThat(modifiedSchedule.getTitle()).isEqualTo("수정된 제목");
        assertThat(modifiedSchedule.getStartDateTime()).isEqualTo(LocalDateTime.of(2024, 1, 3, 14, 0));

        // 3. 새로운 반복 규칙 확인
        RecurrenceRule newRule = recurrenceRuleRepository.findById(newRuleId).orElseThrow();
        assertThat(newRule.getRecurrenceType()).isEqualTo(RecurrenceType.WEEKLY);
        assertThat(newRule.getMaxOccurrences()).isEqualTo(3);

        // 4. 기존 규칙에 연결된 일정들 확인 (1/1, 1/2만 남아있어야 함)
        java.util.List<RecurringSchedule> originalSchedules = recurringScheduleRepository.findByRecurrenceRuleId(originalRuleId);
        assertThat(originalSchedules).hasSize(2);

        // 5. 새 규칙에 연결된 일정들 확인 (1/3, 1/4, 1/5)
        java.util.List<RecurringSchedule> newSchedules = recurringScheduleRepository.findByRecurrenceRuleId(newRuleId);
        assertThat(newSchedules).hasSize(3);
        assertThat(newSchedules).allMatch(s -> s.getTitle().equals("수정된 제목"));
    }

    @Test
    @DisplayName("THIS_AND_FOLLOWING: 반복 규칙은 유지하고 제목·시간만 변경하면 기존 일정들과 수정된 일정들이 동일한 규칙을 갖는다")
    void modifyThisAndFollowing_WithoutRecurrenceRuleChange_ShouldHaveSameRule() {
        // given
        LocalDateTime startDateTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2024, 1, 1, 11, 0);

        // 반복 일정 생성 (5개)
        RecurringScheduleCreateResponse createResponse = createDailyRecurringSchedule(
                "매일 회의", startDateTime, endDateTime, 5);
        
        Long originalRuleId = createResponse.getRecurrenceRule().getId();
        Long targetScheduleId = createResponse.getSchedules().get(2).getId(); // 3번째 일정 선택

        RecurringScheduleModifyRequest request = RecurringScheduleModifyRequest.builder()
                .title("수정된 제목")
                .description("수정된 설명")
                .category("수정된 카테고리")
                .startDateTime(LocalDateTime.of(2024, 1, 3, 14, 0))
                .endDateTime(LocalDateTime.of(2024, 1, 3, 15, 0))
                .visibility(Visibility.PRIVATE)
                .editType(RecurrenceEditType.THIS_AND_FOLLOWING)
                .recurrenceRuleModifyRequest(null) // 반복 규칙 변경 없음
                .build();

        // when
        RecurringScheduleModifyResponse response = recurringScheduleService.modifySchedule(targetScheduleId, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getModifiedCount()).isEqualTo(3);

        // 1. 새로운 반복 규칙이 생성되었는지 확인 (기존 규칙 복사)
        assertThat(recurrenceRuleRepository.count()).isEqualTo(2);

        // 2. 수정된 일정들이 새 반복 규칙에 연결되었는지 확인
        RecurringSchedule modifiedSchedule = recurringScheduleRepository.findById(targetScheduleId).orElseThrow();
        Long newRuleId = modifiedSchedule.getRecurrenceRuleId();
        
        assertThat(newRuleId).isNotEqualTo(originalRuleId); // 새로운 규칙 ID
        assertThat(modifiedSchedule.getTitle()).isEqualTo("수정된 제목");

        // 3. 새로운 반복 규칙이 기존 규칙과 동일한지 확인 (복사된 것)
        RecurrenceRule originalRule = recurrenceRuleRepository.findById(originalRuleId).orElseThrow();
        RecurrenceRule newRule = recurrenceRuleRepository.findById(newRuleId).orElseThrow();
        
        assertThat(newRule.getRecurrenceType()).isEqualTo(originalRule.getRecurrenceType());
        assertThat(newRule.getMaxOccurrences()).isEqualTo(originalRule.getMaxOccurrences());
        assertThat(newRule.getEndDate()).isEqualTo(originalRule.getEndDate());

        // 4. 기존 규칙에 연결된 일정들 확인 (1/1, 1/2만 남아있어야 함)
        java.util.List<RecurringSchedule> originalSchedules = recurringScheduleRepository.findByRecurrenceRuleId(originalRuleId);
        assertThat(originalSchedules).hasSize(2);
        assertThat(originalSchedules).allMatch(s -> s.getTitle().equals("매일 회의")); // 원래 제목 유지

        // 5. 새 규칙에 연결된 일정들 확인 (1/3, 1/4, 1/5)
        java.util.List<RecurringSchedule> newSchedules = recurringScheduleRepository.findByRecurrenceRuleId(newRuleId);
        assertThat(newSchedules).hasSize(3);
        assertThat(newSchedules).allMatch(s -> s.getTitle().equals("수정된 제목"));
    }

    // 테스트 헬퍼 메서드
    private RecurringScheduleCreateResponse createDailyRecurringSchedule(String title, 
                                                                          LocalDateTime startDateTime, 
                                                                          LocalDateTime endDateTime, 
                                                                          int maxOccurrences) {
        RecurrenceRuleCreateRequest ruleRequest = RecurrenceRuleCreateRequest.builder()
                .recurrenceType(RecurrenceType.DAILY)
                .maxOccurrences(maxOccurrences)
                .endDate(startDateTime.toLocalDate().plusDays(maxOccurrences))
                .build();

        RecurringScheduleCreateRequest request = RecurringScheduleCreateRequest.builder()
                .title(title)
                .description("설명")
                .category("카테고리")
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .visibility(Visibility.PUBLIC)
                .recurrenceRuleCreateRequest(ruleRequest)
                .build();

        return recurringScheduleService.createSchedule(request);
    }

}