package com.studdit.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studdit.review.request.ReviewCreateRequest;
import com.studdit.review.request.ReviewModifyRequest;
import com.studdit.review.response.ReviewResponse;
import com.studdit.review.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        ReviewController.class
})
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회고를 생성한다.")
    void createReview() throws Exception{
        // given
        ReviewCreateRequest request = ReviewCreateRequest.builder()
                .content("오늘은 열심히 했다")
                .difficulty(4)
                .tags(List.of("알고리즘", "취준"))
                .build();

        // when & then
        mockMvc.perform(
                        post("/schedules/1/review")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("회고를 조회한다.")
    void findReview() throws Exception{
        // given
        ReviewResponse result = ReviewResponse.builder()
                .reviewId(1L)
                .scheduleId(1L)
                .tags(List.of("알고리즘", "그래프", "DFS", "BFS"))
                .difficulty(4)
                .content("그래프는 너무 어렵다.")
                .build();
        when(reviewService.findReview(1L, 1L))
                .thenReturn(result);


        // when & then
        mockMvc.perform(
                        get("/schedules/1/review/1")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data.reviewId").value(1L))
                .andExpect(jsonPath("$.data.scheduleId").value(1L))
                .andExpect(jsonPath("$.data.difficulty").value(4))
                .andExpect(jsonPath("$.data.content").value("그래프는 너무 어렵다."))
                .andExpect(jsonPath("$.data.tags").isArray())
                .andExpect(jsonPath("$.data.tags.length()").value(4))
                .andExpect(jsonPath("$.data.tags[0]").value("알고리즘"))
                .andExpect(jsonPath("$.data.tags[1]").value("그래프"))
                .andExpect(jsonPath("$.data.tags[2]").value("DFS"))
                .andExpect(jsonPath("$.data.tags[3]").value("BFS"));
    }

    @Test
    @DisplayName("회고를 수정한다.")
    void modifyReview() throws Exception{
        // given
        ReviewModifyRequest request = ReviewModifyRequest.builder()
                .content("오늘은 열심히 했다")
                .difficulty(4)
                .tags(List.of("알고리즘", "취준"))
                .build();

        // when & then
        mockMvc.perform(
                        put("/schedules/1/review/1")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}