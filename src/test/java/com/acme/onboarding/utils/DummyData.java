package com.acme.onboarding.utils;

import com.acme.onboarding.database.entity.OnboardedDriverEntity;
import com.acme.onboarding.database.entity.PendingDriverOnboardingEntity;
import com.acme.onboarding.database.entity.RideEntity;
import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import com.acme.onboarding.database.repository.OnboardedDriverRepository;
import com.acme.onboarding.service.model.Address;
import com.acme.onboarding.service.model.Driver;
import com.acme.onboarding.service.model.Ride;

public class DummyData {
    public static Driver getDriver() {
        return Driver.builder()
                .name("Paul")
                .email("paul@gmail.com")
                .mobile("123456789")
                .address(getAddress())
                .ride(getRide())
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

    public static Ride getRide() {
        return Ride.builder()
                .manufacturer("Toyota")
                .model("Supra")
                .build();
    }

    public static RideEntity getRideEntity() {
        return RideEntity.builder()
                .manufacturer("Toyota")
                .model("Supra")
                .build();
    }

    public static PendingDriverOnboardingEntity getDriverEntity(Integer id, OnboardingModule module, ModuleStatus moduleStatus) {
        return PendingDriverOnboardingEntity.builder()
                .id(id)
                .name("Paul")
                .email("paul@gmail.com")
                .mobile("123456789")
                .address(getAddress())
                .ride(getRideEntity())
                .module(module != null ? module : OnboardingModule.DOCUMENT_COLLECTION)
                .moduleStatus(moduleStatus != null ? moduleStatus : ModuleStatus.IN_PROGRESS)
                .build();
    }

    public static OnboardedDriverEntity getOnboardedDriver(Integer id, PendingDriverOnboardingEntity pendingDriverOnboardingEntity) {
        return OnboardedDriverEntity.builder()
                .id(id)
                .name("Paul")
                .email("paul@gmail.com")
                .mobile("123456789")
                .address(getAddress())
                .ride(getRideEntity())
                .pendingDriverOnboardingEntity(pendingDriverOnboardingEntity)
                .build();
    }
}
