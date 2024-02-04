package com.acme.onboarding.controller;


import com.acme.onboarding.controller.request.MarkDriverReadyRequest;
import com.acme.onboarding.controller.request.RegisterDriverRequest;
import com.acme.onboarding.controller.request.UpdateModuleStatusRequest;
import com.acme.onboarding.controller.response.GenericResponse;
import com.acme.onboarding.controller.response.OnboardingStatusResponse;
import com.acme.onboarding.database.entity.OnboardingEntity;
import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    public void testGetDriverOnboardingStatus() throws Exception {
        OnboardingEntity onboardingEntity = DummyData.getOnboardingEntity(1,null,null);  // create a sample OnboardingEntity
        when(driverOnboardingService.getDriverOnboardingStatus(anyInt())).thenReturn(onboardingEntity);

        mockMvc.perform(MockMvcRequestBuilders.get("/driver/onboarding/status")
                        .param("id", "1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.id").value(onboardingEntity.getId()))
                .andExpect(jsonPath("$.data.module").value(onboardingEntity.getModule().toString()))
                .andExpect(jsonPath("$.data.status").value(onboardingEntity.getModuleStatus().toString()));

        verify(driverOnboardingService, times(1)).getDriverOnboardingStatus(anyInt());
    }

    @Test
    public void testUpdateModuleStatus() throws Exception {
        UpdateModuleStatusRequest request = new UpdateModuleStatusRequest(1, OnboardingModule.DOCUMENT_COLLECTION, ModuleStatus.SUCCESS);
        // Assuming your service method returns void
        doNothing().when(driverOnboardingService)
                .updateModuleStatus(anyInt(), any(), any());

        mockMvc.perform(MockMvcRequestBuilders.put("/driver/onboarding/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value("module status updated successfully"));

        verify(driverOnboardingService, times(1))
                .updateModuleStatus(anyInt(), any(), any());
    }

    @Test
    public void testMarkDriverReady() throws Exception {
        MarkDriverReadyRequest request = new MarkDriverReadyRequest(1);
        // Assuming your service method returns void
        doNothing().when(driverOnboardingService).markDriverReady(anyInt());

        mockMvc.perform(MockMvcRequestBuilders.post("/driver/onboarding/ready")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").value("driver status updated successfully"));

        verify(driverOnboardingService, times(1)).markDriverReady(anyInt());
    }
}
