package com.acme.onboarding.controller;

import com.acme.onboarding.controller.request.MarkDriverReadyRequest;
import com.acme.onboarding.controller.request.RegisterDriverRequest;
import com.acme.onboarding.controller.request.UpdateModuleStatusRequest;
import com.acme.onboarding.controller.response.GenericResponse;
import com.acme.onboarding.database.entity.PendingDriverOnboardingEntity;
import com.acme.onboarding.service.interfaces.IDriverOnboardingService;
import com.acme.onboarding.service.model.Driver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("driver")
@Slf4j
public class DriverController {

    @Autowired
    IDriverOnboardingService driverOnboardingService;

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<Driver>> register(@RequestBody RegisterDriverRequest registerDriverRequest) {
        Driver driver = Driver.builder()
                .name(registerDriverRequest.name())
                .ride(registerDriverRequest.ride())
                .email(registerDriverRequest.email())
                .mobile(registerDriverRequest.mobile())
                .address(registerDriverRequest.address())
                .build();
        Driver savedDriver = driverOnboardingService.register(driver);
        GenericResponse<Driver> response = new GenericResponse<>(true, savedDriver);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<GenericResponse<ArrayList<PendingDriverOnboardingEntity>>> getAllDrivers() {
        ArrayList<PendingDriverOnboardingEntity> drivers = driverOnboardingService.getAllDrivers();
        GenericResponse<ArrayList<PendingDriverOnboardingEntity>> response = new GenericResponse<>(true, drivers);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/updateModuleStatus")
    public ResponseEntity<GenericResponse<String>> updateModuleStatus(@RequestBody UpdateModuleStatusRequest updateModuleStatusRequest) {
        boolean isSuccessful = driverOnboardingService.markModuleStatusAndTriggerNextModule(updateModuleStatusRequest.id(), updateModuleStatusRequest.onboardingModule(), updateModuleStatusRequest.moduleStatus());
        if (!isSuccessful) {
            return new ResponseEntity<>(new GenericResponse<>(false, "there was an error updating module status"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new GenericResponse<>(true, "module status updated successfully"), HttpStatus.OK);
    }


    @PostMapping("/ready")
    public ResponseEntity<GenericResponse<String>> markDriverReady(@RequestBody MarkDriverReadyRequest markDriverReadyRequest) {
        driverOnboardingService.markDriverReady(markDriverReadyRequest.id());
        return new ResponseEntity<>(new GenericResponse<>(true, "driver status updated successfully"), HttpStatus.OK);
    }
}
