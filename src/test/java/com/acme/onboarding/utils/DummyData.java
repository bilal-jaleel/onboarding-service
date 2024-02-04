package com.acme.onboarding.utils;

import com.acme.onboarding.controller.request.RegisterDriverRequest;
import com.acme.onboarding.controller.response.OnboardingStatusResponse;
import com.acme.onboarding.database.entity.DriverEntity;
import com.acme.onboarding.database.entity.OnboardingEntity;
import com.acme.onboarding.database.entity.VehicleEntity;
import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import com.acme.onboarding.service.model.Address;
import com.acme.onboarding.service.model.Driver;
import com.acme.onboarding.service.model.Vehicle;

public class DummyData {
    public static Driver getDriver() {
        return Driver.builder()
                .name("Paul")
                .email("paul@gmail.com")
                .mobile("123456789")
                .address(getAddress())
                .vehicle(getVehicle())
                .build();
    }

    public static Address getAddress() {
        return Address.builder()
                .line1("Line 1")
                .line2("Line 2")
                .landmark("Landmark")
                .state("State")
                .country("Country")
                .pincode("123456")
                .build();
    }

    public static Vehicle getVehicle() {
        return Vehicle.builder()
                .manufacturer("Toyota")
                .model("Etios")
                .type("Sedan")
                .build();
    }

    public static VehicleEntity getVehicleEntity() {
        return VehicleEntity.builder()
                .manufacturer("Toyota")
                .model("Etios")
                .type("Sedan")
                .build();
    }

    public static OnboardingEntity getOnboardingEntity(Integer id, OnboardingModule module, ModuleStatus moduleStatus) {
        return OnboardingEntity.builder()
                .id(id)
                .name("Paul")
                .email("paul@gmail.com")
                .mobile("123456789")
                .address(getAddress())
                .vehicleEntity(getVehicleEntity())
                .module(module != null ? module : OnboardingModule.DOCUMENT_COLLECTION)
                .moduleStatus(moduleStatus != null ? moduleStatus : ModuleStatus.IN_PROGRESS)
                .build();
    }

    public static DriverEntity getOnboardedDriver(Integer id, OnboardingEntity onboardingEntity) {
        return DriverEntity.builder()
                .id(id)
                .name("Paul")
                .email("paul@gmail.com")
                .mobile("1234567890")
                .address(getAddress())
                .vehicleEntity(getVehicleEntity())
                .onboardingEntity(onboardingEntity)
                .build();
    }

    public static RegisterDriverRequest getRegisteredDriverRequest(String name){
        return RegisterDriverRequest.builder()
                .name(name)
                .email("paul@gmail.com")
                .mobile("1234567890")
                .address(getAddress())
                .vehicle(getVehicle())
                .build();
    }

    public static OnboardingStatusResponse getOnboardingStatusResponse(Integer id){
        return OnboardingStatusResponse.builder()
                .id(id)
                .module(OnboardingModule.DOCUMENT_COLLECTION)
                .status(ModuleStatus.IN_PROGRESS)
                .build();
    }
}
