package com.studdit.schedule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studdit.schedule.request.ScheduleCreateRequest;
import com.studdit.schedule.enums.Visibility;
import com.studdit.schedule.request.ScheduleModifyRequest;
import com.studdit.schedule.response.ScheduleCreateResponse;
import com.studdit.schedule.response.ScheduleModifyResponse;
import com.studdit.schedule.response.ScheduleResponse;
import com.studdit.schedule.service.ScheduleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {
        ScheduleController.class
})
class SingleScheduleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ScheduleService scheduleService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("학습 일정을 생성한다.")
    void createSchedule() throws Exception{
        // given
        ScheduleCreateRequest request = ScheduleCreateRequest.builder()
                .title("알고리즘 스터디")
                .description("그래프 탐색 알고리즘 공부")
                .category("프로그래밍")
                .startDateTime(LocalDateTime.parse("2025-05-01T12:00:00"))
                .endDateTime(LocalDateTime.parse("2025-05-01T14:00:00"))
                .visibility(Visibility.PRIVATE)
                .build();
                

        // when & then
        mockMvc.perform(
                        post("/schedules")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("학습 일정을 수정한다.")
    void modifySchedule() throws Exception{
        // given
        Long scheduleId = 2L;
        
        ScheduleModifyRequest request = ScheduleModifyRequest.builder()
                .title("알고리즘 스터디 수정")
                .description("그래프 탐색 알고리즘 공부 수정")
                .category("프로그래밍 수정")
                .startDateTime(LocalDateTime.parse("2025-05-01T12:00:00"))
                .endDateTime(LocalDateTime.parse("2025-05-01T14:00:00"))
                .visibility(Visibility.PRIVATE)
                .build();
                

        // when & then
        mockMvc.perform(
                        put("/schedules/" + scheduleId)
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("학습 일정을 유저이름과 조회기간을 이용해 조회한다.")
    void findSchedules() throws Exception{
        // given
        String username = "test1234";
        String view = "month";
        LocalDateTime date = LocalDateTime.parse("2025-05-07T00:00:00");


        // when & then
        mockMvc.perform(
                        get("/schedules")
                                .param("username", username)
                                .param("view", view)
                                .param("date", date.toString())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("학습 일정을 삭제한다.")
    void deleteSchedule() throws Exception {
        // given
        Long scheduleId = 1L;

        // when & then
        mockMvc.perform(
                        delete("/schedules/" + scheduleId)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}