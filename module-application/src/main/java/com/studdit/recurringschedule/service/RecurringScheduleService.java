package com.studdit.recurringschedule.service;

import com.studdit.recurringschedule.RecurrenceRule;
import com.studdit.recurringschedule.RecurrenceRuleRepository;
import com.studdit.recurringschedule.RecurringSchedule;
import com.studdit.recurringschedule.RecurringScheduleRepository;
import com.studdit.recurringschedule.request.RecurringScheduleCreateRequest;
import com.studdit.recurringschedule.response.RecurringScheduleCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecurringScheduleService {

    private final RecurringScheduleRepository recurringScheduleRepository;
    private final RecurrenceRuleRepository recurrenceRuleRepository;
    private final RecurringScheduleGenerator scheduleGenerator;


    @Transactional
    public RecurringScheduleCreateResponse createSchedule(RecurringScheduleCreateRequest request) {
        // 반복 규칙 생성 및 저장
        RecurrenceRule recurrenceRule = request.getRecurrenceRuleCreateRequest().toRecurrenceRuleEntity();
        RecurrenceRule savedRule = recurrenceRuleRepository.save(recurrenceRule);

        // 반복 규칙을 토대로 반복일정 생성
        List<RecurringSchedule> schedules = scheduleGenerator.createRecurringSchedule(request, savedRule);
        
        // 반복 일정 저장
        List<RecurringSchedule> savedSchedules = recurringScheduleRepository.saveAll(schedules);

        return RecurringScheduleCreateResponse.of(savedSchedules, savedRule);
    }
}
