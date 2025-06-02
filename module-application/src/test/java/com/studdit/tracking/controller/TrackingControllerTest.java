package com.studdit.tracking.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.studdit.tracking.enums.TrackingStatus;
import com.studdit.tracking.service.TrackingResponse;
import com.studdit.tracking.service.TrackingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        TrackingController.class
})
class TrackingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private TrackingService trackingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("학습일정의 추적을 시작한다.")
    void createTracking() throws Exception {
        //given
        TrackingResponse result = TrackingResponse.builder()
                .id(1L)
                .scheduleId(1L)
                .status(TrackingStatus.IN_PROGRESS)
                .eventTime(LocalDateTime.now())
                .build();

        when(trackingService.createTracking(1L)).thenReturn(result);

        //when & then
        mockMvc.perform(
                        post("/schedules/1/track/start")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("학습일정의 추적을 정지한다.")
    void pauseTracking() throws Exception {
        //given
        TrackingResponse result = TrackingResponse.builder()
                .id(1L)
                .scheduleId(1L)
                .status(TrackingStatus.PAUSED)
                .eventTime(LocalDateTime.now())
                .build();

        when(trackingService.modifyTracking(1L, 1L, TrackingStatus.PAUSED)).thenReturn(result);

        //when & then
        mockMvc.perform(
                        post("/schedules/1/track/1/stop")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.status").value("PAUSED"));
    }

    @Test
    @DisplayName("학습일정의 추적을 재개한다.")
    void resumeTracking() throws Exception {
        //given
        TrackingResponse result = TrackingResponse.builder()
                .id(1L)
                .scheduleId(1L)
                .status(TrackingStatus.IN_PROGRESS)
                .eventTime(LocalDateTime.now())
                .build();

        when(trackingService.modifyTracking(1L, 1L, TrackingStatus.IN_PROGRESS)).thenReturn(result);


        //when & then
        mockMvc.perform(
                        post("/schedules/1/track/1/resume")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("학습일정의 추적을 완료한다.")
    void completeTracking() throws Exception {
        //given
        TrackingResponse result = TrackingResponse.builder()
                .id(1L)
                .scheduleId(1L)
                .status(TrackingStatus.COMPLETED)
                .eventTime(LocalDateTime.now())
                .build();

        when(trackingService.modifyTracking(1L, 1L, TrackingStatus.COMPLETED)).thenReturn(result);

        //when & then
        mockMvc.perform(
                        post("/schedules/1/track/1/complete")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.status").value("COMPLETED"));
    }


}