package com.acme.onboarding.controller;

import com.acme.onboarding.controller.request.MarkDriverReadyRequest;
import com.acme.onboarding.controller.request.RegisterDriverRequest;
import com.acme.onboarding.controller.request.UpdateModuleStatusRequest;
import com.acme.onboarding.controller.response.GenericResponse;
import com.acme.onboarding.controller.response.OnboardingStatusResponse;
import com.acme.onboarding.database.entity.OnboardingEntity;
import com.acme.onboarding.service.interfaces.IDriverOnboardingService;
import com.acme.onboarding.service.model.Driver;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("driver/onboarding")
@Slf4j
public class OnboardingController {

    @Autowired
    IDriverOnboardingService driverOnboardingService;

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<Driver>> register(@RequestBody @Valid RegisterDriverRequest registerDriverRequest) {

        Driver driver = Driver.builder()
                .name(registerDriverRequest.name())
                .vehicle(registerDriverRequest.vehicle())
                .email(registerDriverRequest.email())
                .mobile(registerDriverRequest.mobile())
                .address(registerDriverRequest.address())
                .build();

        Driver savedDriver = driverOnboardingService.register(driver);
        GenericResponse<Driver> response = new GenericResponse<>(true, savedDriver);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<GenericResponse<OnboardingStatusResponse>> getDriverOnboardingStatus(@RequestParam(name = "id") @NotNull Integer id) {
        OnboardingEntity onboardingEntity = driverOnboardingService.getDriverOnboardingStatus(id);
        OnboardingStatusResponse onboardingStatusResponse = OnboardingStatusResponse
                .builder()
                .id(onboardingEntity.getId())
                .status(onboardingEntity.getModuleStatus())
                .module(onboardingEntity.getModule())
                .build();
        GenericResponse<OnboardingStatusResponse> response = new GenericResponse<>(true, onboardingStatusResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<GenericResponse<String>> updateModuleStatus(@RequestBody @Valid UpdateModuleStatusRequest updateModuleStatusRequest) {
        driverOnboardingService.updateModuleStatus(updateModuleStatusRequest.id(), updateModuleStatusRequest.onboardingModule(), updateModuleStatusRequest.moduleStatus());
        return new ResponseEntity<>(new GenericResponse<>(true, "module status updated successfully"), HttpStatus.OK);
    }


    @PostMapping("/ready")
    public ResponseEntity<GenericResponse<String>> markDriverReady(@RequestBody @Valid MarkDriverReadyRequest markDriverReadyRequest) {
        driverOnboardingService.markDriverReady(markDriverReadyRequest.id());
        return new ResponseEntity<>(new GenericResponse<>(true, "driver status updated successfully"), HttpStatus.OK);
    }
}
