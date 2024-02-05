package com.acme.onboarding.utils;

import com.acme.onboarding.database.entity.OnboardingEntity;
import com.acme.onboarding.database.entity.VehicleEntity;
import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import com.acme.onboarding.service.model.Driver;
import com.acme.onboarding.service.model.Vehicle;

public class Mapper {

    public static OnboardingEntity mapDriverToOnboardingEntity(Driver driver, OnboardingModule onboardingModule, ModuleStatus moduleStatus, VehicleEntity vehicleEntity) {

        return OnboardingEntity.builder()
                .name(driver.name())
                .email(driver.email())
                .mobile(driver.mobile())
                .address(driver.address())
                .vehicleEntity(mapVehicleToEntity(driver.vehicle()))
                .module(onboardingModule)
                .moduleStatus(moduleStatus)
                .vehicleEntity(vehicleEntity)
                .build();
    }

    public static VehicleEntity mapVehicleToEntity(Vehicle vehicle) {

        return VehicleEntity.builder()
                .model(vehicle.model())
                .manufacturer(vehicle.manufacturer())
                .type(vehicle.type())
                .build();
    }

    public static Driver mapOnboardingEntityToDriver(OnboardingEntity onboardingEntity) {
        return Driver.builder()
                .id(onboardingEntity.getId())
                .name(onboardingEntity.getName())
                .email(onboardingEntity.getEmail())
                .mobile(onboardingEntity.getMobile())
                .address(onboardingEntity.getAddress())
                .vehicle(mapEntityToVehicle(onboardingEntity.getVehicleEntity()))
                .build();
    }

    public static Vehicle mapEntityToVehicle(VehicleEntity vehicleEntity) {

        return Vehicle.builder()
                .id(vehicleEntity.getId())
                .model(vehicleEntity.getModel())
                .manufacturer(vehicleEntity.getManufacturer())
                .type(vehicleEntity.getType())
                .build();
    }

}
