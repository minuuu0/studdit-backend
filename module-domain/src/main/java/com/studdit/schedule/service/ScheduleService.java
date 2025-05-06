package com.studdit.schedule.service;


import com.studdit.schedule.request.ScheduleCreateServiceRequest;
import com.studdit.schedule.request.ScheduleModifyServiceRequest;
import com.studdit.schedule.response.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        System.out.println(serviceRequest.getId());
        return ScheduleResponse.of(serviceRequest);
    }
}
