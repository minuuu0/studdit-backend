package com.studdit.schedule.service;


import com.studdit.schedule.domain.RecurrenceRule;
import com.studdit.schedule.domain.Schedule;
import com.studdit.schedule.domain.ScheduleInstance;
import com.studdit.schedule.enums.ScheduleViewType;
import com.studdit.schedule.repository.*;
import com.studdit.schedule.request.RecurrenceRuleCreateServiceRequest;
import com.studdit.schedule.request.ScheduleCreateServiceRequest;
import com.studdit.schedule.request.ScheduleModifyServiceRequest;
import com.studdit.schedule.response.ScheduleCreateResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleInstanceRepsitory scheduleInstanceRepsitory;
    private final RecurrenceRuleRepository recurrenceRuleRepository;
    private final ScheduleInstanceGenerator instanceGenerator;

    // Business
    @Transactional
    public ScheduleCreateResponse createSchedule(ScheduleCreateServiceRequest request) {
        // 1. 마스터 일정 생성
        Schedule schedule = request.toEntity();
        Schedule savedSchedule = scheduleRepository.save(schedule);

        // 2. 일정 인스턴스 생성
        List<ScheduleInstance> instances = createScheduleInstances(savedSchedule.getId(), request);
        scheduleInstanceRepsitory.saveAll(instances);

        return ScheduleCreateResponse.of(savedSchedule, instances);
    }


    @Transactional
    public ScheduleCreateResponse modifySchedule(ScheduleModifyServiceRequest request) {

        Schedule requestSchedule = request.toEntity();
        Schedule schedule = scheduleRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 일정을 찾을 수 없습니다."));

        schedule.update(requestSchedule);

        return ScheduleCreateResponse.builder().build();
    }
/*
    public List<ScheduleResponse> findSchedules(String username, String view, LocalDateTime date) {
        ScheduleViewType viewType = validateAndParseViewType(view);
        DateRange dateRange = calculateDateRange(viewType, date);

        List<Schedule> schedules = scheduleRepository.findByDateRange(dateRange.getStart(), dateRange.getEnd());

        return schedules.stream()
                .map(ScheduleResponse::of)
                .collect(Collectors.toList());
    }
    /*
 */

    @Transactional
    public ScheduleCreateResponse deleteSchedule(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 일정을 찾을 수 없습니다."));

        scheduleRepository.delete(schedule);

        return null;
    }

    // implementation

    private List<ScheduleInstance> createScheduleInstances(Long scheduleId, ScheduleCreateServiceRequest request) {
        if (request.getRecurrenceRuleCreateServiceRequest() == null) {
            // 단일 일정인 경우
            ScheduleInstance singleInstance = instanceGenerator.createSingleInstance(scheduleId, request);
            return List.of(singleInstance);
        } else {
            // 반복 일정인 경우
            RecurrenceRule recurrenceRule = createRecurrenceRule(scheduleId, request.getRecurrenceRuleCreateServiceRequest());
            recurrenceRuleRepository.save(recurrenceRule);
            List<ScheduleInstance> instances = instanceGenerator.createRecurrenceInstances(scheduleId, recurrenceRule, request);
            return instances;
        }
    }

    // to-do RecurrenceRule의 객체를 만드는 책임을 분리해야하지 않을까
    private RecurrenceRule createRecurrenceRule(Long scheduleId, RecurrenceRuleCreateServiceRequest recurrenceRule) {
        return RecurrenceRule.builder()
                .scheduleId(scheduleId)
                .frequency(recurrenceRule.getFrequency())
                .type(recurrenceRule.getType())
                .endDate(recurrenceRule.getEndDate())
                .byWeekday(recurrenceRule.getByWeekday())
                .byMonthday(recurrenceRule.getByMonthday())
                .byMonth(recurrenceRule.getByMonth())
                .maxOccurrences(recurrenceRule.getMaxOccurrences())
                .build();
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