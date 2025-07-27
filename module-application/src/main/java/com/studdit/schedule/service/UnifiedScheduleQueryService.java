package com.studdit.schedule.service;

import com.studdit.recurringschedule.RecurringSchedule;
import com.studdit.recurringschedule.RecurringScheduleRepository;
import com.studdit.schedule.SingleSchedule;
import com.studdit.schedule.SingleScheduleRepository;
import com.studdit.schedule.enums.ScheduleViewType;
import com.studdit.schedule.response.UnifiedScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UnifiedScheduleQueryService {
    
    private final SingleScheduleRepository singleScheduleRepository;
    private final RecurringScheduleRepository recurringScheduleRepository;
    
    /**
     * 단일 일정과 반복 일정을 통합하여 조회
     * 
     * @param username 사용자명 (현재는 사용하지 않지만 향후 권한 체크 등에 활용 가능)
     * @param viewType 조회 타입 (day, week, month)
     * @param referenceDate 기준 날짜
     * @return 통합된 일정 목록 (시작 시간 순으로 정렬)
     */
    public List<UnifiedScheduleResponse> findSchedules(String username, String viewType, LocalDateTime referenceDate) {
        // 1. 뷰 타입 검증 및 파싱
        ScheduleViewType scheduleViewType = validateAndParseViewType(viewType);
        
        // 2. 날짜 범위 계산
        DateRange dateRange = calculateDateRange(scheduleViewType, referenceDate);
        
        // 3. 단일 일정 조회
        List<SingleSchedule> singleSchedules = singleScheduleRepository.findByDateRange(
                dateRange.getStart(), dateRange.getEnd());
        
        // 4. 반복 일정 조회
        List<RecurringSchedule> recurringSchedules = recurringScheduleRepository.findByDateRange(
                dateRange.getStart(), dateRange.getEnd());
        
        // 5. 통합 응답 생성
        List<UnifiedScheduleResponse> unifiedSchedules = new ArrayList<>();
        
        // 단일 일정 변환
        singleSchedules.stream()
                .map(UnifiedScheduleResponse::fromSingleSchedule)
                .forEach(unifiedSchedules::add);
        
        // 반복 일정 변환
        recurringSchedules.stream()
                .map(UnifiedScheduleResponse::fromRecurringSchedule)
                .forEach(unifiedSchedules::add);
        
        // 6. 시작 시간 순으로 정렬
        unifiedSchedules.sort(Comparator.comparing(UnifiedScheduleResponse::getStartDateTime));
        
        return unifiedSchedules;
    }
    
    private ScheduleViewType validateAndParseViewType(String viewType) {
        try {
            return ScheduleViewType.valueOf(viewType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원하지 않는 뷰 타입입니다: " + viewType);
        }
    }
    
    private DateRange calculateDateRange(ScheduleViewType viewType, LocalDateTime referenceDate) {
        return switch (viewType) {
            case DAY -> DateRange.ofDay(referenceDate);
            case WEEK -> DateRange.ofWeek(referenceDate);
            case MONTH -> DateRange.ofMonth(referenceDate);
        };
    }
}