package com.studdit.schedule.service;


import com.studdit.schedule.domain.RecurrenceRule;
import com.studdit.schedule.domain.Schedule;
import com.studdit.schedule.domain.ScheduleInstance;
import com.studdit.schedule.enums.ScheduleModifyType;
import com.studdit.schedule.enums.ScheduleViewType;
import com.studdit.schedule.repository.*;
import com.studdit.schedule.request.RecurrenceRuleCreateServiceRequest;
import com.studdit.schedule.request.ScheduleCreateServiceRequest;
import com.studdit.schedule.request.ScheduleModifyServiceRequest;
import com.studdit.schedule.response.ScheduleCreateResponse;
import com.studdit.schedule.response.ScheduleDeleteResponse;
import com.studdit.schedule.response.ScheduleResponse;
import com.studdit.schedule.response.ScheduleModifyResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Schedule schedule = request.toScheduleEntity();
        Schedule savedSchedule = scheduleRepository.save(schedule);

        List<ScheduleInstance> instances = createScheduleInstances(savedSchedule.getId(), request);
        List<ScheduleInstance> savedInstances = scheduleInstanceRepsitory.saveAll(instances);

        return ScheduleCreateResponse.of(savedSchedule, savedInstances);
    }

    @Transactional
    public ScheduleModifyResponse modifySchedule(ScheduleModifyServiceRequest request) {
        Schedule schedule = scheduleRepository.findById(request.getScheduleId())
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 일정을 찾을 수 없습니다."));
        schedule.update(request.toScheduleEntity());

        ScheduleModifyResponse response = modifyScheduleInstances(schedule, request);
        return response;
    }

    public List<ScheduleResponse> findSchedules(String username, String view, LocalDateTime date) {
        ScheduleViewType viewType = validateAndParseViewType(view);
        DateRange dateRange = calculateDateRange(viewType, date);

        // 범위 조건에 맞는 여러 인스턴스 조회
        List<ScheduleInstance> instances = scheduleInstanceRepsitory.findByDateRange(dateRange.getStart(), dateRange.getEnd());

        List<Long> scheduleIdList = instances.stream()
                .map(ScheduleInstance::getScheduleId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, Schedule> scheduleMap = scheduleRepository.findAllById(scheduleIdList).stream()
                .collect(Collectors.toMap(Schedule::getId, schedule -> schedule));


        return ScheduleResponse.fromInstances(instances, scheduleMap);
    }

    // to-do
    /*
    @Transactional
    public ScheduleDeleteResponse deleteSchedule(Long id) {
    }*/


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
    private ScheduleModifyResponse modifyScheduleInstances(Schedule schedule, ScheduleModifyServiceRequest request) {

        ScheduleInstance updatedInstance = null;

        if (request.getRecurrenceRuleCreateServiceRequest() == null) {
            List<ScheduleInstance> instances = scheduleInstanceRepsitory.findByScheduleId(schedule.getId());

            if (instances.isEmpty()) {
                throw new EntityNotFoundException("해당 일정의 인스턴스를 찾을 수 없습니다.");
            }
            ScheduleInstance originInstance = instances.get(0);
            originInstance.update(request.toScheduleInstanceEntity());
            updatedInstance = originInstance;

        } else {
            // 수정할 일정이 반복일정인 경우
            ScheduleModifyType modifyType = request.getModifyType();


            switch (modifyType) {
                case THIS_ONLY:
                    updatedInstance = modifyThisInstanceOnly(schedule, request);
                // to-do
                case THIS_AND_FUTURE:
                case ALL_OCCURRENCES:
                default:
                    throw new IllegalArgumentException("지원하지 않는 수정 타입입니다: " + modifyType);
            }
        }

        return ScheduleModifyResponse.of(updatedInstance, schedule);
    }

    private ScheduleInstance modifyThisInstanceOnly(Schedule schedule, ScheduleModifyServiceRequest request) {
        List<ScheduleInstance> instances = scheduleInstanceRepsitory.findByScheduleId(schedule.getId());

        if (instances.isEmpty()) {
            throw new EntityNotFoundException("해당 일정의 인스턴스를 찾을 수 없습니다.");
        }
        ScheduleInstance originInstance = instances.get(0);
        originInstance.update(request.toScheduleInstanceEntity());
        return originInstance;
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
