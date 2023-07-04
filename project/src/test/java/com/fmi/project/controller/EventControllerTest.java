package com.fmi.project.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration
public class EventControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @Disabled
    void getUsersByEventAndRole() throws Exception {
        final var request = get("/events/event/{eventName}/roles/{role}",
                "Wedding", "planner")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Disabled
    void getAllEvents() throws Exception {
        final var request = post("/events/{email}", "tsvetina@test.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void getAllTasksByEmail() {
    }

    @Test
    void getEventByName() {
    }

    @Test
    void getAllTasksByEventName() {
    }

    @Test
    void getTaskByName() {
    }

    @Test
    void getAssigneesByTaskName() {
    }

    @Test
    void addEvent() {
    }

    @Test
    void addTaskByEventName() {
    }

    @Test
    void addUserToEvent() {
    }

    @Test
    void addAssigneeToTask() {
    }

    @Test
    void updateEvent() {
    }

    @Test
    void updateTask() {
    }

    @Test
    void deleteEvent() {
    }

    @Test
    void deleteTask() {
    }
}