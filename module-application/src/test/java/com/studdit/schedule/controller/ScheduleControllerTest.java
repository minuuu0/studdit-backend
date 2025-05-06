package com.studdit.schedule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studdit.schedule.request.ScheduleCreateRequest;
import com.studdit.schedule.enums.Visibility;
import com.studdit.schedule.request.ScheduleModifyRequest;
import com.studdit.schedule.service.ScheduleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {
        ScheduleController.class
})
class ScheduleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
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
        ScheduleModifyRequest request = ScheduleModifyRequest.builder()
                .title("알고리즘 스터디")
                .description("그래프 탐색 알고리즘 공부")
                .category("프로그래밍")
                .startDateTime(LocalDateTime.parse("2025-05-01T12:00:00"))
                .endDateTime(LocalDateTime.parse("2025-05-01T14:00:00"))
                .visibility(Visibility.PRIVATE)
                .build();

        // when & then
        mockMvc.perform(
                        put("/schedules/2")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}