package com.acme.onboarding.controller;


import com.acme.onboarding.controller.request.RegisterDriverRequest;
import com.acme.onboarding.controller.response.GenericResponse;
import com.acme.onboarding.service.interfaces.IDriverOnboardingService;
import com.acme.onboarding.service.model.Driver;
import com.acme.onboarding.utils.DummyData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OnboardingController.class)
public class OnboardingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IDriverOnboardingService driverOnboardingService;

    @Test
    public void testRegisterDriverSuccess() throws Exception {
        RegisterDriverRequest registerDriverRequest = DummyData.getRegisteredDriverRequest("Paul");
        Driver driver = DummyData.getDriver();

        GenericResponse<Driver> genericResponse = new GenericResponse<>(true, driver);

        when(driverOnboardingService.register(any())).thenReturn(driver);

        mockMvc.perform(MockMvcRequestBuilders.post("/driver/onboarding/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDriverRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(genericResponse)));
    }

    @Test
    public void testRegisterDriverBadRequest() throws Exception {
        RegisterDriverRequest registerDriverRequest = DummyData.getRegisteredDriverRequest(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/driver/onboarding/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDriverRequest)))
                .andExpect(status().isBadRequest());
    }
}
