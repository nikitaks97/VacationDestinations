package com.example.demo.controller;

import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CustomErrorControllerTest {

    private static final String ERROR_PATH = "/error";
    private static final String ERROR_VIEW = "error";
    private static final String ATTR_STATUS = "status";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_MESSAGE = "message";
    
    private static final String MSG_NOT_FOUND = "The requested resource could not be found";
    private static final String MSG_SERVER_ERROR = "An unexpected error occurred";
    private static final String MSG_FORBIDDEN = "Access to this resource is forbidden";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void test404Error() throws Exception {
        mockMvc.perform(get(ERROR_PATH).with(setErrorAttributes(HttpStatus.NOT_FOUND)))
               .andExpect(status().isOk())
               .andExpect(view().name(ERROR_VIEW))
               .andExpect(model().attribute(ATTR_STATUS, 404))
               .andExpect(model().attribute(ATTR_ERROR, "Not Found"))
               .andExpect(model().attribute(ATTR_MESSAGE, MSG_NOT_FOUND));
    }

    @Test
    void test500Error() throws Exception {
        mockMvc.perform(get(ERROR_PATH).with(setErrorAttributes(HttpStatus.INTERNAL_SERVER_ERROR)))
               .andExpect(status().isOk())
               .andExpect(view().name(ERROR_VIEW))
               .andExpect(model().attribute(ATTR_STATUS, 500))
               .andExpect(model().attribute(ATTR_ERROR, "Internal Server Error"))
               .andExpect(model().attribute(ATTR_MESSAGE, MSG_SERVER_ERROR));
    }

    @Test
    void test403Error() throws Exception {
        mockMvc.perform(get(ERROR_PATH).with(setErrorAttributes(HttpStatus.FORBIDDEN)))
               .andExpect(status().isOk())
               .andExpect(view().name(ERROR_VIEW))
               .andExpect(model().attribute(ATTR_STATUS, 403))
               .andExpect(model().attribute(ATTR_ERROR, "Forbidden"))
               .andExpect(model().attribute(ATTR_MESSAGE, MSG_FORBIDDEN));
    }

    private RequestPostProcessor setErrorAttributes(final HttpStatus status) {
        return request -> {
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, status.value());
            request.setAttribute(RequestDispatcher.ERROR_MESSAGE, status.getReasonPhrase());
            return request;
        };
    }
}
