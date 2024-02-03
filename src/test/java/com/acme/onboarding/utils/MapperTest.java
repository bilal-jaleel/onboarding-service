package com.acme.onboarding.utils;

import com.acme.onboarding.database.entity.OnboardingEntity;
import com.acme.onboarding.database.entity.VehicleEntity;
import com.acme.onboarding.service.model.Driver;
import com.acme.onboarding.service.model.Vehicle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapperTest {


    @Test
    public void testMapDriverToEntity() {
        Driver driver = DummyData.getDriver();
        OnboardingEntity onboardingEntity = Mapper.mapDriverToOnboardingEntity(driver);
        assertEquals(onboardingEntity.getName(), driver.name());
        assertEquals(onboardingEntity.getEmail(), driver.email());
        assertEquals(onboardingEntity.getMobile(), driver.mobile());
    }

    @Test
    public void testMapRideToEntity() {
        Vehicle vehicle = DummyData.getVehicle();
        VehicleEntity vehicleEntity = Mapper.mapVehicleToEntity(vehicle);
        assertEquals(vehicle.manufacturer(), vehicleEntity.getManufacturer());
        assertEquals(vehicle.model(), vehicleEntity.getModel());
    }

    @Test
    public void testMapEntityToRide() {
        VehicleEntity vehicleEntity = DummyData.getVehicleEntity();
        Vehicle vehicle = Mapper.mapEntityToVehicle(vehicleEntity);
        assertEquals(vehicle.manufacturer(), vehicleEntity.getManufacturer());
        assertEquals(vehicle.model(), vehicleEntity.getModel());
    }

    @Test
    public void testMapEntityToDriver() {
        OnboardingEntity onboardingEntity = DummyData.getOnboardingEntity(null, null,null);
        Driver driver = Mapper.mapOnboardingEntityToDriver(onboardingEntity);
        assertEquals(onboardingEntity.getName(), driver.name());
        assertEquals(onboardingEntity.getEmail(), driver.email());
        assertEquals(onboardingEntity.getMobile(), driver.mobile());
    }
}

