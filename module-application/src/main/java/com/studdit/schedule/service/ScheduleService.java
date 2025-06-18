package com.studdit.schedule.service;


import com.studdit.schedule.SingleSchedule;
import com.studdit.schedule.SingleScheduleRepository;
import com.studdit.schedule.enums.ScheduleViewType;
import com.studdit.schedule.request.ScheduleCreateServiceRequest;
import com.studdit.schedule.request.ScheduleModifyServiceRequest;
import com.studdit.schedule.response.ScheduleCreateResponse;
import com.studdit.schedule.response.ScheduleResponse;
import com.studdit.schedule.response.ScheduleModifyResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

    private final SingleScheduleRepository singleScheduleRepository;

    // 단일 일정 생성
    @Transactional
    public ScheduleCreateResponse createSchedule(ScheduleCreateServiceRequest request) {
        SingleSchedule singleSchedule = request.toEntity();
        
        SingleSchedule savedSingleSchedule = singleScheduleRepository.save(singleSchedule);

        return ScheduleCreateResponse.of(savedSingleSchedule);
    }

    // 단일 일정 수정
    @Transactional
    public ScheduleModifyResponse modifySchedule(ScheduleModifyServiceRequest request) {
        SingleSchedule singleSchedule = singleScheduleRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 일정을 찾을 수 없습니다."));
                

        singleSchedule.update(request.toEntity());
        
        return ScheduleModifyResponse.of(singleSchedule);
    }

    // 일정 조회 (단일 일정만 조회)
    public List<ScheduleResponse> findSchedules(String username, String view, LocalDateTime date) {
        ScheduleViewType viewType = validateAndParseViewType(view);
        DateRange dateRange = calculateDateRange(viewType, date);

        // 범위 조건에 맞는 단일 일정 조회
        List<SingleSchedule> singleSchedules = singleScheduleRepository.findByDateRange(
                dateRange.getStart(), dateRange.getEnd());

        return singleSchedules.stream()
                .map(ScheduleResponse::of)
                .collect(Collectors.toList());
    }

    // 단일 일정 삭제
    @Transactional
    public ScheduleResponse deleteSchedule(Long scheduleId) {
        SingleSchedule singleSchedule = singleScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 일정을 찾을 수 없습니다."));

        
        // 일정 인스턴스 삭제
        singleScheduleRepository.deleteById(scheduleId);
        return null;
    }

    private ScheduleViewType validateAndParseViewType(String view) {
        try {
            return ScheduleViewType.valueOf(view.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원하지 않는 뷰 타입입니다: " + view);
        }
    }

    private DateRange calculateDateRange(ScheduleViewType viewType, LocalDateTime date) {
        switch (viewType) {
            case DAY:
                return DateRange.ofDay(date);
            case WEEK:
                return DateRange.ofWeek(date);
            case MONTH:
                return DateRange.ofMonth(date);
            default:
                throw new IllegalArgumentException("지원하지 않는 뷰 타입입니다: " + viewType);
        }
    }
}
