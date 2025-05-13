package com.studdit.schedule.service;


import com.studdit.schedule.enums.ScheduleViewType;
import com.studdit.schedule.repository.Schedule;
import com.studdit.schedule.repository.ScheduleRepository;
import com.studdit.schedule.request.ScheduleCreateServiceRequest;
import com.studdit.schedule.request.ScheduleModifyServiceRequest;
import com.studdit.schedule.response.ScheduleResponse;
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

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ScheduleResponse createSchedule(ScheduleCreateServiceRequest request) {

        Schedule schedule = request.toEntity();
        // 현재는 request를 반환하지만 추후엔 저장한 엔티티를 반환한다.
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return ScheduleResponse.of(savedSchedule);
    }


    @Transactional
    public ScheduleResponse modifySchedule(ScheduleModifyServiceRequest request) {
        Schedule schedule = scheduleRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 일정을 찾을 수 없습니다."));

        schedule.update(request);

        return ScheduleResponse.of(schedule);
    }

    public List<ScheduleResponse> findSchedules(String username, String view, LocalDateTime date) {
        ScheduleViewType viewType = validateAndParseViewType(view);
        DateRange dateRange = calculateDateRange(viewType, date);

        List<Schedule> schedules = scheduleRepository.findByDateRange(dateRange.getStart(), dateRange.getEnd());

        return schedules.stream()
                .map(ScheduleResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public ScheduleResponse deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 일정을 찾을 수 없습니다."));

        scheduleRepository.delete(schedule);

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
