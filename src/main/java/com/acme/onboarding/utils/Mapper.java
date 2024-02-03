package com.acme.onboarding.utils;

import com.acme.onboarding.database.entity.PendingDriverOnboardingEntity;
import com.acme.onboarding.database.entity.RideEntity;
import com.acme.onboarding.service.model.Driver;
import com.acme.onboarding.service.model.Ride;

public class Mapper {

    public static PendingDriverOnboardingEntity mapDriverToEntity(Driver driver) {

        return PendingDriverOnboardingEntity.builder()
                .name(driver.name())
                .email(driver.email())
                .mobile(driver.mobile())
                .address(driver.address())
                .ride(mapRideToEntity(driver.ride()))
                .build();
    }

    public static RideEntity mapRideToEntity(Ride ride) {

        return RideEntity.builder()
                .model(ride.model())
                .manufacturer(ride.manufacturer())
                .build();
    }

    public static Driver mapEntityToDriver(PendingDriverOnboardingEntity pendingDriverOnboardingEntity) {
        return Driver.builder()
                .name(pendingDriverOnboardingEntity.getName())
                .email(pendingDriverOnboardingEntity.getEmail())
                .mobile(pendingDriverOnboardingEntity.getMobile())
                .address(pendingDriverOnboardingEntity.getAddress())
                .ride(mapEntityToRide(pendingDriverOnboardingEntity.getRide()))
                .build();
    }

    public static Ride mapEntityToRide(RideEntity rideEntity) {

        return Ride.builder()
                .model(rideEntity.getModel())
                .manufacturer(rideEntity.getManufacturer())
                .build();
    }

}
