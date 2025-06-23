package com.studdit.recurringschedule.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studdit.recurringschedule.request.RecurrenceRuleCreateRequest;
import com.studdit.recurringschedule.request.RecurringScheduleCreateRequest;
import com.studdit.recurringschedule.service.RecurringScheduleService;
import com.studdit.schedule.enums.RecurrenceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(RecurringScheduleController.class)
class RecurringScheduleControllerTest {

}