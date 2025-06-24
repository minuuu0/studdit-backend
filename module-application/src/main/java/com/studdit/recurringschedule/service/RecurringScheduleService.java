package com.studdit.recurringschedule.service;

import com.studdit.recurringschedule.RecurrenceRule;
import com.studdit.recurringschedule.RecurrenceRuleRepository;
import com.studdit.recurringschedule.RecurringSchedule;
import com.studdit.recurringschedule.RecurringScheduleRepository;
import com.studdit.recurringschedule.request.RecurringScheduleCreateRequest;
import com.studdit.recurringschedule.request.RecurringScheduleModifyRequest;
import com.studdit.recurringschedule.response.RecurringScheduleCreateResponse;
import com.studdit.recurringschedule.response.RecurringScheduleModifyResponse;
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

    @Transactional
    public RecurringScheduleModifyResponse modifySchedule(Long scheduleId, RecurringScheduleModifyRequest request) {
        RecurringSchedule targetSchedule = recurringScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 반복 일정을 찾을 수 없습니다."));

        List<RecurringSchedule> modifiedSchedules;

        switch (request.getEditType()) {
            case THIS_ONLY -> {
                // 현재 일정만 수정
                modifiedSchedules = modifyThisOnlySchedule(targetSchedule, request);
            }
            case THIS_AND_FOLLOWING -> {
                // 현재 일정 및 향후 일정 수정
                modifiedSchedules = modifyThisAndFollowingSchedules(targetSchedule, request);
            }
            case ALL -> {
                // 모든 일정 수정
                modifiedSchedules = modifyAllSchedules(targetSchedule, request);
            }
            default -> throw new IllegalArgumentException("지원하지 않는 수정 유형입니다.");
        }

        return RecurringScheduleModifyResponse.of(modifiedSchedules);
    }

    private List<RecurringSchedule> modifyThisOnlySchedule(RecurringSchedule targetSchedule, RecurringScheduleModifyRequest request) {
        // 규칙 1: "이 일정만" 수정은 예외 처리
        // 반복 규칙은 그대로 유지하고, 해당 일정만 예외적으로 다른 내용을 가짐
        // 반복 규칙 변경은 허용하지 않음 (비즈니스 규칙)
        
        if (request.getRecurrenceRuleModifyRequest() != null) {
            throw new IllegalArgumentException("개별 일정에는 반복 규칙을 변경할 수 없습니다.");
        }
        
        // 일반 속성만 수정 (JPA 변경감지)
        targetSchedule.update(
                request.getTitle(),
                request.getDescription(),
                request.getCategory(),
                request.getStartDateTime(),
                request.getEndDateTime(),
                request.getVisibility()
        );
        
        return List.of(targetSchedule);
    }

    private List<RecurringSchedule> modifyThisAndFollowingSchedules(RecurringSchedule targetSchedule, RecurringScheduleModifyRequest request) {
        Long originalRecurrenceRuleId = targetSchedule.getRecurrenceRuleId();
        
        // 현재 일정 및 향후 일정 조회 (현재 일정 포함)
        List<RecurringSchedule> schedulesToUpdate = recurringScheduleRepository
                .findByRecurrenceRuleIdAndStartDateTimeAfter(originalRecurrenceRuleId, targetSchedule.getStartDateTime().minusSeconds(1));
        
        if (request.getRecurrenceRuleModifyRequest() != null) {
            // 규칙 2: 반복 규칙 변경은 새 시리즈 생성
            // 시리즈를 분할하고, 향후 일정들에 새로운 반복 규칙을 적용한 새 시리즈를 만듦
            
            // 1. 새로운 반복 규칙 생성
            RecurrenceRule newRule = request.getRecurrenceRuleModifyRequest().toRecurrenceRuleEntity();
            RecurrenceRule savedNewRule = recurrenceRuleRepository.save(newRule);
            
            // 2. 향후 일정들을 새 반복 규칙에 연결하고 속성 업데이트
            for (RecurringSchedule schedule : schedulesToUpdate) {
                schedule.update(
                        request.getTitle(),
                        request.getDescription(),
                        request.getCategory(),
                        request.getStartDateTime(),
                        request.getEndDateTime(),
                        request.getVisibility()
                );
                // 새로운 반복 규칙에 연결
                schedule.updateRecurrenceRuleId(savedNewRule.getId());
            }
            
            return schedulesToUpdate;
            
        } else {
            // 규칙 3: 일반 속성 변경은 시리즈 분할
            // 같은 반복 규칙을 사용하는 새 시리즈를 만들어서 향후 일정들에 새로운 속성을 적용
            
            // 1. 기존 반복 규칙을 복사한 새로운 반복 규칙 생성
            RecurrenceRule originalRule = recurrenceRuleRepository.findById(originalRecurrenceRuleId)
                    .orElseThrow(() -> new IllegalArgumentException("반복 규칙을 찾을 수 없습니다."));
            
            RecurrenceRule newRule = RecurrenceRule.builder()
                    .recurrenceType(originalRule.getRecurrenceType())
                    .maxOccurrences(originalRule.getMaxOccurrences())
                    .endDate(originalRule.getEndDate())
                    .build();
            RecurrenceRule savedNewRule = recurrenceRuleRepository.save(newRule);
            
            // 2. 향후 일정들을 새 반복 규칙에 연결하고 속성 업데이트
            for (RecurringSchedule schedule : schedulesToUpdate) {
                schedule.update(
                        request.getTitle(),
                        request.getDescription(),
                        request.getCategory(),
                        request.getStartDateTime(),
                        request.getEndDateTime(),
                        request.getVisibility()
                );
                // 새로운 반복 규칙에 연결 (같은 규칙이지만 분할된 시리즈)
                schedule.updateRecurrenceRuleId(savedNewRule.getId());
            }
            
            return schedulesToUpdate;
        }
    }

    private List<RecurringSchedule> modifyAllSchedules(RecurringSchedule targetSchedule, RecurringScheduleModifyRequest request) {
        Long recurrenceRuleId = targetSchedule.getRecurrenceRuleId();
        
        // 모든 관련 일정 조회
        List<RecurringSchedule> allSchedules = recurringScheduleRepository.findByRecurrenceRuleId(recurrenceRuleId);
        
        if (request.getRecurrenceRuleModifyRequest() != null) {
            // 규칙 4: 반복 규칙 변경 - 기존 반복 규칙 직접 업데이트
            RecurrenceRule existingRule = recurrenceRuleRepository.findById(recurrenceRuleId)
                    .orElseThrow(() -> new IllegalArgumentException("반복 규칙을 찾을 수 없습니다."));
            
            existingRule.update(
                    request.getRecurrenceRuleModifyRequest().getRecurrenceType(),
                    request.getRecurrenceRuleModifyRequest().getMaxOccurrences(),
                    request.getRecurrenceRuleModifyRequest().getEndDate()
            );
        }
        
        // 규칙 4: 일반 속성 변경 - 기존 일정들 직접 수정
        for (RecurringSchedule schedule : allSchedules) {
            schedule.update(
                    request.getTitle(),
                    request.getDescription(),
                    request.getCategory(),
                    request.getStartDateTime(),
                    request.getEndDateTime(),
                    request.getVisibility()
            );
        }
        
        return allSchedules;
    }
}
