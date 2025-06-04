package com.studdit.schedule.service;

import com.studdit.schedule.domain.RecurrenceRule;
import com.studdit.schedule.domain.ScheduleInstance;
import com.studdit.schedule.enums.RecurrenceType;
import com.studdit.schedule.enums.Visibility;
import com.studdit.schedule.domain.Schedule;
import com.studdit.schedule.repository.RecurrenceRuleRepository;
import com.studdit.schedule.repository.ScheduleInstanceRepsitory;
import com.studdit.schedule.repository.ScheduleRepository;
import com.studdit.schedule.request.RecurrenceRuleCreateServiceRequest;
import com.studdit.schedule.request.ScheduleCreateServiceRequest;
import com.studdit.schedule.request.ScheduleModifyServiceRequest;
import com.studdit.schedule.response.ScheduleCreateResponse;
import com.studdit.schedule.response.ScheduleInstanceResponse;
import com.studdit.schedule.response.ScheduleModifyResponse;
import com.studdit.schedule.response.ScheduleResponse;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private ScheduleInstanceRepsitory scheduleInstanceRepsitory;

    @Mock
    private RecurrenceRuleRepository recurrenceRuleRepository;

    @Mock
    private ScheduleInstanceGenerator instanceGenerator;

    @InjectMocks
    private ScheduleService scheduleService;


    @Test
    @DisplayName("단일 일정을 생성한다.")
    void createSingleSchedule() {
        //given
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 4, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 4, 11, 0);
        ScheduleCreateServiceRequest request = ScheduleCreateServiceRequest.builder()
                .title("제목")
                .description("일정 내용")
                .startDateTime(startTime)
                .endDateTime(endTime)
                .category("프로그래밍")
                .visibility(Visibility.PUBLIC)
                .build();

        Schedule savedSchedule = Schedule.builder()
                .id(1L)
                .title("단일 일정")
                .description("단일 일정 내용")
                .category("회의")
                .build();

        ScheduleInstance singleInstance = ScheduleInstance.builder()
                .id(1L)
                .scheduleId(1L)
                .startDateTime(startTime)
                .endDateTime(endTime)
                .visibility(Visibility.PRIVATE)
                .build();

        when(scheduleRepository.save(any(Schedule.class))).thenReturn(savedSchedule);
        when(instanceGenerator.createSingleInstance(anyLong(), any())).thenReturn(singleInstance);
        when(scheduleInstanceRepsitory.saveAll(any())).thenReturn(List.of(singleInstance));

        //when
        ScheduleCreateResponse response = scheduleService.createSchedule(request);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("단일 일정");
        assertThat(response.getDescription()).isEqualTo("단일 일정 내용");
        assertThat(response.getCategory()).isEqualTo("회의");

        assertThat(response.getInstances()).hasSize(1);
        ScheduleInstanceResponse resultInstance = response.getInstances().get(0);
        assertThat(resultInstance.getId()).isEqualTo(1L);
        assertThat(resultInstance.getScheduleId()).isEqualTo(1L);
        assertThat(resultInstance.getVisibility()).isEqualTo(Visibility.PRIVATE);
        assertThat(resultInstance.getStartDateTime()).isEqualTo(startTime);
        assertThat(resultInstance.getEndDateTime()).isEqualTo(endTime);
    }

    @Test
    @DisplayName("반복 일정을 생성하면 여러 인스턴스가 포함된 응답이 반환된다")
    void createRecurringSchedule() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 4, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 4, 10, 0);

        RecurrenceRuleCreateServiceRequest recurrenceRequest = RecurrenceRuleCreateServiceRequest.builder()
                .frequency(1)
                .type(RecurrenceType.DAILY)
                .maxOccurrences(3)
                .byWeekday("mo,we,fr")
                .build();

        ScheduleCreateServiceRequest request = ScheduleCreateServiceRequest.builder()
                .title("반복 일정")
                .description("매일 운동")
                .startDateTime(startTime)
                .endDateTime(endTime)
                .category("운동")
                .visibility(Visibility.PRIVATE)
                .recurrenceRuleCreateServiceRequest(recurrenceRequest)
                .build();

        // 실제 Schedule 객체
        Schedule savedSchedule = Schedule.builder()
                .id(2L)
                .title("반복 일정")
                .description("매일 운동")
                .category("운동")
                .build();

        // 실제 RecurrenceRule 객체
        RecurrenceRule savedRule = RecurrenceRule.builder()
                .id(1L)
                .scheduleId(2L)
                .frequency(1)
                .type(RecurrenceType.DAILY)
                .maxOccurrences(3)
                .byWeekday("mo,we,fr")
                .build();

        // 실제 ScheduleInstance 객체들 - 각각 직접 생성
        ScheduleInstance instance1 = ScheduleInstance.builder()
                .id(1L)
                .scheduleId(2L)
                .startDateTime(startTime)
                .endDateTime(endTime)
                .visibility(Visibility.PRIVATE)
                .build();

        ScheduleInstance instance2 = ScheduleInstance.builder()
                .id(2L)
                .scheduleId(2L)
                .startDateTime(startTime.plusDays(2)) // 수요일
                .endDateTime(endTime.plusDays(2))
                .visibility(Visibility.PRIVATE)
                .build();

        ScheduleInstance instance3 = ScheduleInstance.builder()
                .id(3L)
                .scheduleId(2L)
                .startDateTime(startTime.plusDays(4)) // 금요일
                .endDateTime(endTime.plusDays(4))
                .visibility(Visibility.PRIVATE)
                .build();

        List<ScheduleInstance> instances = List.of(instance1, instance2, instance3);

        when(scheduleRepository.save(any(Schedule.class))).thenReturn(savedSchedule);
        when(recurrenceRuleRepository.save(any(RecurrenceRule.class))).thenReturn(savedRule);
        when(instanceGenerator.createRecurrenceInstances(anyLong(), any(), any())).thenReturn(instances);
        when(scheduleInstanceRepsitory.saveAll(any())).thenReturn(instances);

        // when
        ScheduleCreateResponse response = scheduleService.createSchedule(request);

        // then - 상태 검증
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getTitle()).isEqualTo("반복 일정");
        assertThat(response.getDescription()).isEqualTo("매일 운동");

        assertThat(response.getInstances()).hasSize(3);

        // 각 인스턴스가 모두 같은 스케줄 ID를 가지는지 확인
        for (ScheduleInstanceResponse instance : response.getInstances()) {
            assertThat(instance.getScheduleId()).isEqualTo(2L);
            assertThat(instance.getVisibility()).isEqualTo(Visibility.PRIVATE);
        }

        // 날짜가 순차적으로 증가하는지 확인
        List<LocalDateTime> startTimes = response.getInstances().stream()
                .map(ScheduleInstanceResponse::getStartDateTime)
                .sorted()
                .toList();

        assertThat(startTimes.get(0)).isEqualTo(startTime);
        assertThat(startTimes.get(1)).isEqualTo(startTime.plusDays(2));
        assertThat(startTimes.get(2)).isEqualTo(startTime.plusDays(4));
    }

    @Test
    @DisplayName("단일 일정을 수정한다")
    void modifySingleSchedule() {
        // given

        LocalDateTime startTime = LocalDateTime.of(2025, 6, 4, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 4, 11, 0);

        ScheduleModifyServiceRequest request = ScheduleModifyServiceRequest.builder()
                .scheduleId(1L)
                .title("수정 제목")
                .description("수정 내용")
                .startDateTime(startTime.plusHours(1))
                .endDateTime(endTime.plusHours(1))
                .category("수정 카테고리")
                .visibility(Visibility.PUBLIC)
                .build();

        Schedule existingSchedule = Schedule.builder()
                .id(1L)
                .title("기존 제목")
                .description("기존 내용")
                .category("기존 카테고리")
                .build();

        ScheduleInstance existingInstance = ScheduleInstance.builder()
                .id(1L)
                .scheduleId(1L)
                .startDateTime(startTime)
                .endDateTime(endTime)
                .visibility(Visibility.PUBLIC)
                .build();

        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(existingSchedule));
        when(scheduleInstanceRepsitory.findByScheduleId(1L)).thenReturn(List.of(existingInstance));

        // when
        ScheduleModifyResponse response = scheduleService.modifySchedule(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("수정 제목");
        assertThat(response.getDescription()).isEqualTo("수정 내용");
        assertThat(response.getCategory()).isEqualTo("수정 카테고리");

        assertThat(response.getInstances()).hasSize(1);
        ScheduleInstanceResponse resultInstance = response.getInstances().get(0);
        assertThat(resultInstance.getId()).isEqualTo(1L);
        assertThat(resultInstance.getScheduleId()).isEqualTo(1L);
        assertThat(resultInstance.getVisibility()).isEqualTo(Visibility.PUBLIC);
        assertThat(resultInstance.getStartDateTime()).isEqualTo(startTime.plusHours(1));
        assertThat(resultInstance.getEndDateTime()).isEqualTo(endTime.plusHours(1));
    }

    @Test
    @DisplayName("존재하지 않는 일정을을 수정하려고 하면 예외가 발생한다.")
    void modifyScheduleNotFound() {
        //given
        Long id = 999L;
        ScheduleModifyServiceRequest request = ScheduleModifyServiceRequest.builder()
                .scheduleId(id)
                .title("수정 제목")
                .description("수정 내용")
                .visibility(Visibility.PRIVATE)
                .build();

        when(scheduleRepository.findById(id)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> scheduleService.modifySchedule(request))
                .isInstanceOf(EntityNotFoundException.class);
    }

}
