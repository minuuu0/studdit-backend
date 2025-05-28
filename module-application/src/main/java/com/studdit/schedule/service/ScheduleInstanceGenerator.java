package com.studdit.schedule.service;

import com.studdit.schedule.domain.RecurrenceRule;
import com.studdit.schedule.domain.Schedule;
import com.studdit.schedule.domain.ScheduleInstance;
import com.studdit.schedule.enums.VariantType;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleInstanceGenerator {

    private static final int DEFAULT_SCHEDULE_GENERATION_MONTHS = 3;
    private static final int DEFAULT_MAX_INSTANCES = 1000;

    // 인스턴스 생성 메서드

    /*
    로직 흐름
    1. 반복 일정의 인스턴스 요청
    2. 인스턴스 생성 범위 설정
    3. 반복 인스턴스 생성
    4. 다음 반복 시간 계산
    5. 생성 종료 조건 확인 후 생성된 인스턴스 반환
     */
    public List<ScheduleInstance> generateInstances(
            RecurrenceRule rule,
            LocalDateTime startDateTime,
            Duration duration
    ) {
        List<ScheduleInstance> instances = new ArrayList<>();
        // 현재 처리중인 인스턴스의 시작 시간
        LocalDateTime currentDateTime = startDateTime;
        // 일정 인스턴스를 생성할 마지막 시점
        LocalDateTime generationUntil = startDateTime.plusMonths(DEFAULT_SCHEDULE_GENERATION_MONTHS);


        while (currentDateTime.isBefore(generationUntil) && shouldContinueGenerating(rule, instances.size(), currentDateTime)) {
            instances.add(ScheduleInstance.builder()
                    .scheduleId(rule.getScheduleId())
                    .startDateTime(currentDateTime)
                    .endDateTime(currentDateTime.plus(duration))
                    .variantType(VariantType.ORIGINAL)
                    .build());

            // 이후 반복일정 계산
            currentDateTime = calculateNextOccurrence(currentDateTime, rule);

            // 종료 조건 체크
            if (rule.getEndDate() != null && currentDateTime.isAfter(rule.getEndDate().atStartOfDay())) {
                break;
            }
            if (rule.getMaxOccurrences() != null && instances.size() >= rule.getMaxOccurrences()) {
                break;
            }
        }
        return instances;
    }

    private LocalDateTime calculateNextOccurrence(LocalDateTime currentDateTime, RecurrenceRule rule) {

        // to-do 예외처리
        switch (rule.getType()) {
            case DAILY:
                return currentDateTime.plusDays(1);

            case WEEKLY:
                return currentDateTime.plusWeeks(rule.getFrequency() != null ? rule.getFrequency() : 1);

            case MONTHLY:
                return currentDateTime.plusMonths(rule.getFrequency() != null ? rule.getFrequency() : 1);

            case YEARLY:
                return currentDateTime.plusYears(rule.getFrequency() != null ? rule.getFrequency() : 1);

            default:
                return currentDateTime.plusDays(1);
        }
    }

    private boolean shouldContinueGenerating(RecurrenceRule rule, int currentCount, LocalDateTime currentDateTime) {
        // 최대 발생 횟수 체크
        if (rule.getMaxOccurrences() != null && currentCount >= rule.getMaxOccurrences()) {
            return false;
        }

        // 종료 날짜 체크
        if (rule.getEndDate() != null &&
                currentDateTime.isAfter(rule.getEndDate().atStartOfDay())) {
            return false;
        }

        // 무한 반복 방지 (최대 1000개 인스턴스)
        if (currentCount >= DEFAULT_MAX_INSTANCES) {
            return false;
        }

        return true;
    }
}
