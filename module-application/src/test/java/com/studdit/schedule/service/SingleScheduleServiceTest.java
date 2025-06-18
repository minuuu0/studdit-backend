package com.studdit.schedule.service;

import com.studdit.schedule.SingleSchedule;
import com.studdit.schedule.SingleScheduleRepository;
import com.studdit.schedule.enums.Visibility;
import com.studdit.schedule.request.ScheduleCreateServiceRequest;
import com.studdit.schedule.request.ScheduleModifyServiceRequest;
import com.studdit.schedule.response.ScheduleCreateResponse;
import com.studdit.schedule.response.ScheduleModifyResponse;
import com.studdit.schedule.response.ScheduleResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SingleScheduleServiceTest {

    @Mock
    private SingleScheduleRepository singleScheduleRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    @Test
    @DisplayName("단일 일정을 생성한다.")
    void createSingleSchedule() {
        // given
        LocalDateTime startTime = LocalDateTime.of(2025, 6, 4, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 6, 4, 11, 0);
        
        ScheduleCreateServiceRequest request = ScheduleCreateServiceRequest.builder()
                .title("알고리즘 스터디")
                .description("그래프 탐색 알고리즘 공부")
                .startDateTime(startTime)
                .endDateTime(endTime)
                .category("프로그래밍")
                .visibility(Visibility.PUBLIC)
                .build();

        SingleSchedule savedSingleSchedule = SingleSchedule.builder()
                .id(1L)
                .title("알고리즘 스터디")
                .description("그래프 탐색 알고리즘 공부")
                .startDateTime(startTime)
                .endDateTime(endTime)
                .category("프로그래밍")
                .visibility(Visibility.PUBLIC)
                .build();

        when(singleScheduleRepository.save(any(SingleSchedule.class))).thenReturn(savedSingleSchedule);

        // when
        ScheduleCreateResponse response = scheduleService.createSchedule(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("알고리즘 스터디");
        assertThat(response.getDescription()).isEqualTo("그래프 탐색 알고리즘 공부");
        assertThat(response.getCategory()).isEqualTo("프로그래밍");
        assertThat(response.getStartDateTime()).isEqualTo(startTime);
        assertThat(response.getEndDateTime()).isEqualTo(endTime);
        assertThat(response.getVisibility()).isEqualTo(Visibility.PUBLIC);
        
        verify(singleScheduleRepository, times(1)).save(any(SingleSchedule.class));
    }

    @Test
    @DisplayName("단일 일정을 수정한다.")
    void modifySingleSchedule() {
        // given
        Long scheduleId = 1L;
        LocalDateTime originalStartTime = LocalDateTime.of(2025, 6, 4, 10, 0);
        LocalDateTime originalEndTime = LocalDateTime.of(2025, 6, 4, 11, 0);
        LocalDateTime newStartTime = LocalDateTime.of(2025, 6, 4, 14, 0);
        LocalDateTime newEndTime = LocalDateTime.of(2025, 6, 4, 16, 0);
        
        SingleSchedule existingSingleSchedule = SingleSchedule.builder()
                .id(scheduleId)
                .title("기존 제목")
                .description("기존 내용")
                .startDateTime(originalStartTime)
                .endDateTime(originalEndTime)
                .category("기존 카테고리")
                .visibility(Visibility.PRIVATE)
                .build();
        
        ScheduleModifyServiceRequest request = ScheduleModifyServiceRequest.builder()
                .id(scheduleId)
                .title("수정된 제목")
                .description("수정된 내용")
                .startDateTime(newStartTime)
                .endDateTime(newEndTime)
                .category("수정된 카테고리")
                .visibility(Visibility.PUBLIC)
                .build();

        when(singleScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(existingSingleSchedule));

        // when
        ScheduleModifyResponse response = scheduleService.modifySchedule(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(scheduleId);
        assertThat(response.getTitle()).isEqualTo("수정된 제목");
        assertThat(response.getDescription()).isEqualTo("수정된 내용");
        assertThat(response.getCategory()).isEqualTo("수정된 카테고리");
        assertThat(response.getStartDateTime()).isEqualTo(newStartTime);
        assertThat(response.getEndDateTime()).isEqualTo(newEndTime);
        assertThat(response.getVisibility()).isEqualTo(Visibility.PUBLIC);
        
        // 원본 객체가 실제로 수정되었는지 확인
        assertThat(existingSingleSchedule.getTitle()).isEqualTo("수정된 제목");
        assertThat(existingSingleSchedule.getDescription()).isEqualTo("수정된 내용");
        assertThat(existingSingleSchedule.getCategory()).isEqualTo("수정된 카테고리");
        assertThat(existingSingleSchedule.getStartDateTime()).isEqualTo(newStartTime);
        assertThat(existingSingleSchedule.getEndDateTime()).isEqualTo(newEndTime);
        assertThat(existingSingleSchedule.getVisibility()).isEqualTo(Visibility.PUBLIC);
        
        verify(singleScheduleRepository, times(1)).findById(scheduleId);
    }

    @Test
    @DisplayName("존재하지 않는 일정을 수정하려고 하면 예외가 발생한다.")
    void modifyNonExistentScheduleShouldThrowException() {
        // given
        Long nonExistentId = 999L;
        
        ScheduleModifyServiceRequest request = ScheduleModifyServiceRequest.builder()
                .id(nonExistentId)
                .title("수정된 제목")
                .build();

        when(singleScheduleRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> scheduleService.modifySchedule(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("해당 ID의 일정을 찾을 수 없습니다");
        
        verify(singleScheduleRepository, times(1)).findById(nonExistentId);
    }

    @Test
    @DisplayName("날짜 범위로 일정을 조회한다.")
    void findSchedulesByDateRange() {
        // given
        LocalDateTime referenceDate = LocalDateTime.of(2025, 6, 15, 0, 0);
        LocalDateTime rangeStart = LocalDateTime.of(2025, 6, 1, 0, 0);
        LocalDateTime rangeEnd = LocalDateTime.of(2025, 6, 30, 23, 59, 59);

        SingleSchedule singleSchedule1 = SingleSchedule.builder()
                .id(1L)
                .title("6월 초 일정")
                .startDateTime(LocalDateTime.of(2025, 6, 5, 10, 0))
                .endDateTime(LocalDateTime.of(2025, 6, 5, 11, 0))
                .category("회의")
                .visibility(Visibility.PUBLIC)
                .build();

        SingleSchedule singleSchedule2 = SingleSchedule.builder()
                .id(2L)
                .title("6월 중순 일정")
                .startDateTime(LocalDateTime.of(2025, 6, 15, 14, 0))
                .endDateTime(LocalDateTime.of(2025, 6, 15, 16, 0))
                .category("스터디")
                .visibility(Visibility.PRIVATE)
                .build();

        SingleSchedule singleSchedule3 = SingleSchedule.builder()
                .id(3L)
                .title("6월 말 일정")
                .startDateTime(LocalDateTime.of(2025, 6, 28, 9, 0))
                .endDateTime(LocalDateTime.of(2025, 6, 28, 12, 0))
                .category("프로젝트")
                .visibility(Visibility.PUBLIC)
                .build();

        List<SingleSchedule> singleSchedules = List.of(singleSchedule1, singleSchedule2, singleSchedule3);

        when(singleScheduleRepository.findByDateRange(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(singleSchedules);

        // when
        List<ScheduleResponse> responses = scheduleService.findSchedules("testuser", "month", referenceDate);

        // then
        assertThat(responses).hasSize(3);

        // 첫 번째 일정 검증
        assertThat(responses.get(0).getId()).isEqualTo(1L);
        assertThat(responses.get(0).getTitle()).isEqualTo("6월 초 일정");
        assertThat(responses.get(0).getStartDateTime()).isEqualTo(LocalDateTime.of(2025, 6, 5, 10, 0));

        // 두 번째 일정 검증
        assertThat(responses.get(1).getId()).isEqualTo(2L);
        assertThat(responses.get(1).getTitle()).isEqualTo("6월 중순 일정");
        assertThat(responses.get(1).getVisibility()).isEqualTo(Visibility.PRIVATE);

        // 세 번째 일정 검증
        assertThat(responses.get(2).getId()).isEqualTo(3L);
        assertThat(responses.get(2).getTitle()).isEqualTo("6월 말 일정");
        assertThat(responses.get(2).getCategory()).isEqualTo("프로젝트");

        verify(singleScheduleRepository, times(1)).findByDateRange(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    // to-do
    // DateRange의 범위를 검증해야할거같은데 Private 메서드라서 검증을 어떻게 할까..?



    
    @Test
    @DisplayName("일정을 삭제한다.")
    void deleteSchedule() {
        // given
        Long scheduleId = 1L;
        
        SingleSchedule existingSingleSchedule = SingleSchedule.builder()
                .id(scheduleId)
                .title("삭제할 일정")
                .build();

        when(singleScheduleRepository.findById(scheduleId)).thenReturn(Optional.of(existingSingleSchedule));
        doNothing().when(singleScheduleRepository).deleteById(scheduleId);

        // when
        scheduleService.deleteSchedule(scheduleId);

        // then
        verify(singleScheduleRepository, times(1)).findById(scheduleId);
        verify(singleScheduleRepository, times(1)).deleteById(scheduleId);
    }

    
    @Test
    @DisplayName("존재하지 않는 일정을 삭제하려고 하면 예외가 발생한다.")
    void deleteNonExistentScheduleShouldThrowException() {
        // given
        Long nonExistentId = 999L;

        when(singleScheduleRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> scheduleService.deleteSchedule(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("해당 ID의 일정을 찾을 수 없습니다");
        
        verify(singleScheduleRepository, times(1)).findById(nonExistentId);
        verify(singleScheduleRepository, never()).deleteById(any());
    }
    
    @Test
    @DisplayName("지원하지 않는 뷰 타입으로 일정을 조회하려고 하면 예외가 발생한다.")
    void findSchedulesWithInvalidViewTypeShouldThrowException() {
        // given
        String invalidViewType = "invalid_type";
        LocalDateTime date = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> scheduleService.findSchedules("testuser", invalidViewType, date))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지원하지 않는 뷰 타입입니다");
    }
}