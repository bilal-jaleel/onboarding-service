package com.acme.onboarding.utils;

import com.acme.onboarding.database.entity.PendingDriverOnboardingEntity;
import com.acme.onboarding.database.entity.RideEntity;
import com.acme.onboarding.service.model.Driver;
import com.acme.onboarding.service.model.Ride;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapperTest {


    @Test
    public void testMapDriverToEntity() {
        Driver driver = DummyData.getDriver();
        PendingDriverOnboardingEntity pendingDriverOnboardingEntity = Mapper.mapDriverToEntity(driver);
        assertEquals(pendingDriverOnboardingEntity.getName(), driver.name());
        assertEquals(pendingDriverOnboardingEntity.getEmail(), driver.email());
        assertEquals(pendingDriverOnboardingEntity.getMobile(), driver.mobile());
    }

    @Test
    public void testMapRideToEntity() {
        Ride ride = DummyData.getRide();
        RideEntity rideEntity = Mapper.mapRideToEntity(ride);
        assertEquals(ride.manufacturer(), rideEntity.getManufacturer());
        assertEquals(ride.model(), rideEntity.getModel());
    }

    @Test
    public void testMapEntityToRide() {
        RideEntity rideEntity = DummyData.getRideEntity();
        Ride ride= Mapper.mapEntityToRide(rideEntity);
        assertEquals(ride.manufacturer(), rideEntity.getManufacturer());
        assertEquals(ride.model(), rideEntity.getModel());
    }

    @Test
    public void testMapEntityToDriver() {
        PendingDriverOnboardingEntity pendingDriverOnboardingEntity = DummyData.getDriverEntity(null, null,null);
        Driver driver = Mapper.mapEntityToDriver(pendingDriverOnboardingEntity);
        assertEquals(pendingDriverOnboardingEntity.getName(), driver.name());
        assertEquals(pendingDriverOnboardingEntity.getEmail(), driver.email());
        assertEquals(pendingDriverOnboardingEntity.getMobile(), driver.mobile());
    }
}

