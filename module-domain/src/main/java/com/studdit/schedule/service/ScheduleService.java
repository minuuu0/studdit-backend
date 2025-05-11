package com.studdit.schedule.service;


import com.studdit.schedule.request.ScheduleCreateServiceRequest;
import com.studdit.schedule.request.ScheduleModifyServiceRequest;
import com.studdit.schedule.response.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleService {

    @Transactional
    public ScheduleResponse createSchedule(ScheduleCreateServiceRequest serviceRequest) {

        // scheduleRepository.save();

        // 현재는 request를 반환하지만 추후엔 저장한 엔티티를 반환한다.
        return ScheduleResponse.of(serviceRequest);
    }


    @Transactional
    public ScheduleResponse modifySchedule(ScheduleModifyServiceRequest serviceRequest) {
        // 수정 구현
        return ScheduleResponse.of(serviceRequest);
    }

    public List<ScheduleResponse> findSchedules(String username, String view, LocalDate date) {
        List<ScheduleResponse> list = Arrays.asList(
                ScheduleResponse.builder()
                        .id(1L)
                        .title("1번")
                        .description("1번 설명입니다.")
                        .build(),
                ScheduleResponse.builder()
                        .id(2L)
                        .title("2번")
                        .description("2번 설명입니다.")
                        .build(),
                ScheduleResponse.builder()
                        .id(1L)
                        .title("3번")
                        .description("3번 설명입니다.")
                        .build()
        );
        return list;
    }

    public ScheduleResponse deleteSchedule(Long id) {
        // delete
        return null;
    }
}
