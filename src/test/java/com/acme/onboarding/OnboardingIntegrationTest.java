package com.acme.onboarding;

import com.acme.onboarding.controller.request.MarkDriverReadyRequest;
import com.acme.onboarding.controller.request.RegisterDriverRequest;
import com.acme.onboarding.controller.request.UpdateModuleStatusRequest;
import com.acme.onboarding.service.interfaces.IDriverOnboardingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class OnboardingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IDriverOnboardingService driverOnboardingService;

    // Store created entities to clean up after each test
    private int driverId;

    @Test
    void testOnboardingWorkflow() throws Exception {
        // Step 1: Register a driver
        RegisterDriverRequest registerRequest = new RegisterDriverRequest(/* provide necessary details */);
        MvcResult registerResult = mockMvc.perform(MockMvcRequestBuilders.post("/driver/onboarding/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // Extract driver ID from the registration result
        driverId = Integer.parseInt(registerResult.getResponse().getContentAsString());

        // Step 2: Get driver onboarding status
        mockMvc.perform(MockMvcRequestBuilders.get("/driver/onboarding/status")
                        .param("id", String.valueOf(driverId)))
                .andExpect(status().isOk());

        // Step 3: Update module status
        UpdateModuleStatusRequest updateRequest = new UpdateModuleStatusRequest(/* provide necessary details */);
        mockMvc.perform(MockMvcRequestBuilders.put("/driver/onboarding/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());

        // Step 4: Mark driver as ready
        MarkDriverReadyRequest readyRequest = new MarkDriverReadyRequest(/* provide necessary details */);
        mockMvc.perform(MockMvcRequestBuilders.post("/driver/onboarding/ready")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(readyRequest)))
                .andExpect(status().isOk());
    }

    @AfterEach
    void cleanup() {
        // Rollback transactions and clean up created entries
        driverOnboardingService.deleteDriver(driverId);
    }
}
