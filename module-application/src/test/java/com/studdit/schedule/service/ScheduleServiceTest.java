package com.studdit.schedule.service;

import com.studdit.schedule.enums.Visibility;
import com.studdit.schedule.domain.Schedule;
import com.studdit.schedule.repository.ScheduleRepository;
import com.studdit.schedule.request.ScheduleCreateServiceRequest;
import com.studdit.schedule.request.ScheduleModifyServiceRequest;
import com.studdit.schedule.response.ScheduleCreateResponse;
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

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    @DisplayName("일정을 생성한다.")
    void createSchedule() {
        //given
        ScheduleCreateServiceRequest request = ScheduleCreateServiceRequest.builder()
                .title("제목")
                .description("일정 내용")
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(2))
                .category("프로그래밍")
                .visibility(Visibility.PUBLIC)
                .build();

        Schedule schedule = request.toEntity();

        Schedule savedSchedule = Schedule.builder()
                .id(1L)
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .visibility(schedule.getVisibility())
                .build();

        when(scheduleRepository.save(any(Schedule.class))).thenReturn(savedSchedule);

        //when
        ScheduleCreateResponse response = scheduleService.createSchedule(request);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(savedSchedule.getId());
        assertThat(response.getTitle()).isEqualTo(savedSchedule.getTitle());
        assertThat(response.getDescription()).isEqualTo(savedSchedule.getDescription());
        assertThat(response.getVisibility()).isEqualTo(savedSchedule.getVisibility());
    }


    @Test
    @DisplayName("일정을 수정한다.")
    void modifySchedule() {
        //given
        Long scheduleId = 1L;
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusHours(2);

        Schedule originalSchedule = Schedule.builder()
                .id(scheduleId)
                .title("기존 제목")
                .description("기존 내용")
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .visibility(Visibility.PUBLIC)
                .build();

        ScheduleModifyServiceRequest request = ScheduleModifyServiceRequest.builder()
                .id(scheduleId)
                .title("수정 제목")
                .description("수정 내용")
                .visibility(Visibility.PRIVATE)
                .build();

        when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(originalSchedule));

        //when
        ScheduleCreateResponse response = scheduleService.modifySchedule(request);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(scheduleId);
        assertThat(response.getTitle()).isEqualTo("수정 제목");
        assertThat(response.getDescription()).isEqualTo("수정 내용");
        assertThat(response.getStartDateTime()).isEqualTo(startDateTime.plusHours(1));
        assertThat(response.getEndDateTime()).isEqualTo(endDateTime.plusHours(1));
        assertThat(response.getVisibility()).isEqualTo(Visibility.PRIVATE);
    }

    @Test
    @DisplayName("존재하지 않는 일정을을 수정하려고 하면 예외가 발생한다.")
    void modifyScheduleNotFound() {
        //given
        Long id = 999L;
        ScheduleModifyServiceRequest request = ScheduleModifyServiceRequest.builder()
                .id(id)
                .title("수정 제목")
                .description("수정 내용")
                .visibility(Visibility.PRIVATE)
                .build();

        when(scheduleRepository.findById(id)).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> scheduleService.modifySchedule(request))
                .isInstanceOf(EntityNotFoundException.class);

    }

    @Test
    @DisplayName("일정을 조회한다.")
    void findSchedules() {
        //given
        String username = "testUser";
        String view = "DAY";
        LocalDateTime date = LocalDateTime.of(2025, 5, 13, 10, 0);

        // 하루 범위에 대한 시작과 끝 시간
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59);

        Schedule schedule1 = Schedule.builder()
                .id(1L)
                .title("일정 1")
                .description("내용 1")
                .startDateTime(startOfDay)
                .endDateTime(startOfDay.plusHours(1))
                .visibility(Visibility.PUBLIC)
                .build();

        Schedule schedule2 = Schedule.builder()
                .id(2L)
                .title("일정 2")
                .description("내용 2")
                .startDateTime(endOfDay.minusHours(1))
                .endDateTime(endOfDay)
                .visibility(Visibility.PRIVATE)
                .build();

        List<Schedule> schedules = Arrays.asList(schedule1, schedule2);

        when(scheduleRepository.findByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(schedules);

        //when
        List<ScheduleCreateResponse> responses = scheduleService.findSchedules(username, view, date);
        //then
        Assertions.assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
        assertThat(responses.get(0).getTitle()).isEqualTo("일정 1");
        assertThat(responses.get(1).getId()).isEqualTo(2L);
        assertThat(responses.get(1).getTitle()).isEqualTo("일정 2");

    }

    @Test
    @DisplayName("일간 뷰로 일정을 조회한다 - 날짜 범위 계산 검증")
    void findSchedules_Day_DateRangeCalculation() {
        // given
        String username = "testUser";
        String view = "DAY";
        LocalDateTime date = LocalDateTime.of(2025, 5, 13, 10, 0);

        LocalDateTime expectedStartDate = date.toLocalDate().atStartOfDay();
        LocalDateTime expectedEndDate = date.toLocalDate().atTime(LocalTime.MAX);

        List<Schedule> schedules = List.of();

        // when
        List<ScheduleCreateResponse> result = scheduleService.findSchedules(username, view, date);

        // then
        // 1. 리포지토리 메소드가 정확한 날짜 범위로 호출되었는지 검증
        verify(scheduleRepository).findByDateRange(expectedStartDate, expectedEndDate);
    }

    @Test
    @DisplayName("주간 뷰로 일정을 조회한다 - 날짜 범위 계산 검증")
    void findSchedules_Week_DateRangeCalculation() {
        // given
        String username = "testUser";
        String view = "week";
        LocalDateTime date = LocalDateTime.of(2025, 5, 13, 10, 0); // 화요일

        // 해당 주의 시작일(일요일)과 종료일(토요일) 계산
        LocalDate baseDate = date.toLocalDate();
        LocalDate monday = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate sunday = baseDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        LocalDateTime expectedStartDate = monday.atStartOfDay();
        LocalDateTime expectedEndDate = sunday.atTime(LocalTime.MAX);

        List<Schedule> schedules = List.of();

        // when
        List<ScheduleCreateResponse> result = scheduleService.findSchedules(username, view, date);

        // then
        // 리포지토리 메소드가 정확한 날짜 범위로 호출되었는지 검증
        verify(scheduleRepository).findByDateRange(expectedStartDate, expectedEndDate);
    }

    @Test
    @DisplayName("월간 뷰로 일정을 조회한다 - 날짜 범위 계산 검증")
    void findSchedules_Month_DateRangeCalculation() {
        // given
        String username = "testUser";
        String view = "month";
        LocalDateTime date = LocalDateTime.of(2025, 5, 13, 10, 0); // 5월 13일

        // 월간 뷰의 시작일: 해당 월 첫날의 주의 일요일
        LocalDate monthStart = date.toLocalDate().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate startDate = monthStart.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));

        // 월간 뷰의 종료일: 해당 월 마지막날의 주의 토요일
        LocalDate monthEnd = date.toLocalDate().with(TemporalAdjusters.lastDayOfMonth());
        LocalDate endDate = monthEnd.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        // 예상되는 시작/종료 일시
        LocalDateTime expectedStartDate = startDate.atStartOfDay();
        LocalDateTime expectedEndDate = endDate.atTime(LocalTime.MAX);

        // when
        List<ScheduleCreateResponse> result = scheduleService.findSchedules(username, view, date);

        // then
        verify(scheduleRepository).findByDateRange(expectedStartDate, expectedEndDate);
    }

    @Test
    @DisplayName("잘못된 뷰 타입이 주어지면 예외가 발생한다")
    void findSchedulesIllegalArgumentException() {
        // given
        String username = "testUser";
        String invalidView = "invalidView";
        LocalDateTime date = LocalDateTime.of(2025, 5, 13, 10, 0);

        // when & then
        assertThatThrownBy(() -> scheduleService.findSchedules(username, invalidView, date))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("일정을 삭제한다")
    void deleteSchedule() {
        //given
        Long id = 1L;
        Schedule schedule = Schedule.builder()
                .id(id)
                .title("제목")
                .description("내용")
                .startDateTime(LocalDateTime.now())
                .endDateTime(LocalDateTime.now().plusHours(2))
                .visibility(Visibility.PUBLIC)
                .build();

        when(scheduleRepository.findById(id)).thenReturn(Optional.of(schedule));
        //when
        ScheduleCreateResponse response = scheduleService.deleteSchedule(id);

        //then
        assertThat(response).isNull();
        verify(scheduleRepository).delete(schedule);
    }


}