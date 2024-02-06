package com.acme.onboarding;

import com.acme.onboarding.controller.request.MarkDriverReadyRequest;
import com.acme.onboarding.controller.request.RegisterDriverRequest;
import com.acme.onboarding.controller.request.UpdateModuleStatusRequest;
import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import com.acme.onboarding.service.interfaces.IDriverOnboardingService;
import com.acme.onboarding.utils.TestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class OnboardingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IDriverOnboardingService driverOnboardingService;

    private static Integer driverId;


    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Execution(ExecutionMode.SAME_THREAD)
    class OnboardingFlow {

        @Test
        @Order(1)
        void testRegisterDriverSuccess() throws Exception {
            // Step 1: Register a driver
            RegisterDriverRequest registerDriverRequest = TestData.getRegisteredDriverRequest("Paul");

            MvcResult registerDriverResponse = mockMvc.perform(MockMvcRequestBuilders.post("/driver/onboarding/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(registerDriverRequest)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
            // Extract driver ID from the registration result
            driverId = JsonPath.read(registerDriverResponse.getResponse().getContentAsString(), "$.data.id");
        }

        @Test
        @Order(2)
        void testUpdateDocumentCollectionStatusSuccess() throws Exception {

            // Step 2: Update Document Collection Status as success
            UpdateModuleStatusRequest updateModuleRequest = TestData.getUpdateModuleStatusRequest(driverId, OnboardingModule.DOCUMENT_COLLECTION, ModuleStatus.SUCCESS);
            mockMvc.perform(MockMvcRequestBuilders.put("/driver/onboarding/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateModuleRequest)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @Order(3)
        void testUpdateBackgroundVerificationStatusSuccess() throws Exception {

            // Step 3: Update Background Verification Status as success
            UpdateModuleStatusRequest updateModuleRequest = TestData.getUpdateModuleStatusRequest(driverId, OnboardingModule.BACKGROUND_VERIFICATION, ModuleStatus.SUCCESS);

            mockMvc.perform(MockMvcRequestBuilders.put("/driver/onboarding/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateModuleRequest)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @Order(4)
        void testUpdateTrackerShippingStatusSuccess() throws Exception {
            // Step 4: Update Tracker Shipping Status as success
            UpdateModuleStatusRequest updateModuleRequest = TestData.getUpdateModuleStatusRequest(driverId, OnboardingModule.TRACKER_SHIPPING, ModuleStatus.SUCCESS);
             mockMvc.perform(MockMvcRequestBuilders.put("/driver/onboarding/update")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateModuleRequest)))
                     .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @Order(5)
        void testGetOnboardingStatusSuccess() throws Exception {


            // Step 5: Check Driver Status is onboarded
             mockMvc.perform(MockMvcRequestBuilders.get("/driver/onboarding/status")
                            .param("id", String.valueOf(driverId)))
                     .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(true))
                    .andExpect(jsonPath("$.data.module").value(OnboardingModule.ONBOARDED.toString()))
                    .andExpect(jsonPath("$.data.status").value(ModuleStatus.SUCCESS.toString()));

        }

        @Test
        @Order(6)
        void testMarkDriverReadySuccess() throws Exception {
            // Step 6: Mark driver as ready
            MarkDriverReadyRequest markDriverReadyRequest = new MarkDriverReadyRequest(driverId);

            mockMvc.perform(MockMvcRequestBuilders.post("/driver/onboarding/ready")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(markDriverReadyRequest)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

    }
}
