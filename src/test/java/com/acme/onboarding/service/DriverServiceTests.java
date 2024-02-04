package com.acme.onboarding.service;

import com.acme.onboarding.database.entity.DriverEntity;
import com.acme.onboarding.database.entity.OnboardingEntity;
import com.acme.onboarding.database.entity.VehicleEntity;
import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import com.acme.onboarding.database.repository.DriverRepository;
import com.acme.onboarding.database.repository.OnboardingRepository;
import com.acme.onboarding.database.repository.VehicleRepository;
import com.acme.onboarding.utils.DummyData;
import jakarta.validation.ValidationException;
import com.acme.onboarding.service.implementation.DriverOnboardingService;
import com.acme.onboarding.service.model.Driver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DriverServiceTests {


    @Mock
    private OnboardingRepository pendingOnboardingRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private DriverRepository driverRepository;

    @Autowired
    @InjectMocks
    private DriverOnboardingService driverOnboardingService;


    @Test
    void testCreateDriverSuccess() {
        Driver driver = DummyData.getDriver();
        OnboardingEntity onboardingEntity = DummyData.getOnboardingEntity(null, null, null);
        VehicleEntity vehicleEntity = DummyData.getVehicleEntity();

        when(pendingOnboardingRepository.save(any())).thenReturn(onboardingEntity);
        when(vehicleRepository.findByManufacturerAndModel(anyString(), anyString())).thenReturn(vehicleEntity);

        Driver savedDriver = driverOnboardingService.register(driver);

        assertEquals(driver.name(), savedDriver.name());
        assertEquals(driver.email(), savedDriver.email());
        assertEquals(driver.mobile(), savedDriver.mobile());
        verify(pendingOnboardingRepository, times(1)).save(onboardingEntity);
    }

    @Test
    void testGetDriverOnboardingStatusSuccess() {
        OnboardingEntity onboardingEntity = DummyData.getOnboardingEntity(null,null,null);

        Integer driverId = onboardingEntity.getId();

        when(pendingOnboardingRepository.getReferenceById(driverId)).thenReturn(onboardingEntity);

        OnboardingEntity driverOnboardingStatus = driverOnboardingService.getDriverOnboardingStatus(driverId);

        assertEquals(onboardingEntity, driverOnboardingStatus);
        verify(pendingOnboardingRepository, times(1)).getReferenceById(driverId);
    }


    @Test
    void testUpdateDocumentCollectionSuccess() {

        OnboardingEntity onboardingEntity = DummyData.getOnboardingEntity(1, OnboardingModule.DOCUMENT_COLLECTION, ModuleStatus.SUCCESS);

        int driverID = onboardingEntity.getId();

        doNothing().when(pendingOnboardingRepository).updateModuleStatusForDriver(driverID, OnboardingModule.BACKGROUND_VERIFICATION, ModuleStatus.IN_PROGRESS);
        when(pendingOnboardingRepository.getReferenceById(driverID)).thenReturn(onboardingEntity);

        driverOnboardingService.updateModuleStatus(driverID, OnboardingModule.DOCUMENT_COLLECTION, ModuleStatus.SUCCESS);

        verify(pendingOnboardingRepository, times(1)).updateModuleStatusForDriver(driverID, OnboardingModule.BACKGROUND_VERIFICATION, ModuleStatus.IN_PROGRESS);
    }

    @Test
    void testInvalidModuleUpdate() {

        OnboardingEntity onboardingEntity = DummyData.getOnboardingEntity(1, OnboardingModule.DOCUMENT_COLLECTION, ModuleStatus.IN_PROGRESS);

        int driverID = onboardingEntity.getId();

        when(pendingOnboardingRepository.getReferenceById(driverID)).thenReturn(onboardingEntity);

        assertThrows(ValidationException.class,
                () -> driverOnboardingService.updateModuleStatus(driverID, OnboardingModule.BACKGROUND_VERIFICATION, ModuleStatus.SUCCESS)
        );
        verify(pendingOnboardingRepository, times(1)).getReferenceById(driverID);
    }

    @Test
    void testModuleUpdationOfOnboardedDriver() {

        OnboardingEntity onboardingEntity = DummyData.getOnboardingEntity(1, OnboardingModule.ONBOARDED, ModuleStatus.SUCCESS);

        int driverID = onboardingEntity.getId();

        when(pendingOnboardingRepository.getReferenceById(driverID)).thenReturn(onboardingEntity);

        assertThrows(ValidationException.class,
                () -> driverOnboardingService.updateModuleStatus(driverID, OnboardingModule.BACKGROUND_VERIFICATION, ModuleStatus.SUCCESS)
        );
        verify(pendingOnboardingRepository, times(1)).getReferenceById(driverID);
    }

    @Test
    void testOnboardedModuleStatusUpdate() {

        OnboardingEntity onboardingEntity = DummyData.getOnboardingEntity(1, OnboardingModule.TRACKER_SHIPPING, ModuleStatus.IN_PROGRESS);

        int driverID = onboardingEntity.getId();

        when(pendingOnboardingRepository.getReferenceById(driverID)).thenReturn(onboardingEntity);
        doNothing().when(pendingOnboardingRepository).updateModuleStatusForDriver(driverID, OnboardingModule.ONBOARDED, ModuleStatus.SUCCESS);

        driverOnboardingService.updateModuleStatus(driverID, OnboardingModule.TRACKER_SHIPPING, ModuleStatus.SUCCESS);
        verify(pendingOnboardingRepository, times(1)).getReferenceById(driverID);
        verify(pendingOnboardingRepository, times(1)).updateModuleStatusForDriver(driverID,OnboardingModule.ONBOARDED, ModuleStatus.SUCCESS);
    }

    @Test
    void testModuleFailureStatusUpdation() {
        OnboardingEntity onboardingEntity = DummyData.getOnboardingEntity(1, OnboardingModule.TRACKER_SHIPPING, ModuleStatus.IN_PROGRESS);
        int driverID = onboardingEntity.getId();

        when(pendingOnboardingRepository.getReferenceById(driverID)).thenReturn(onboardingEntity);
        doNothing().when(pendingOnboardingRepository).updateModuleStatusForDriver(driverID, OnboardingModule.TRACKER_SHIPPING, ModuleStatus.FAILED);

        driverOnboardingService.updateModuleStatus(driverID, OnboardingModule.TRACKER_SHIPPING, ModuleStatus.FAILED);
        verify(pendingOnboardingRepository, times(1)).getReferenceById(driverID);
        verify(pendingOnboardingRepository, times(1)).updateModuleStatusForDriver(driverID,OnboardingModule.TRACKER_SHIPPING, ModuleStatus.FAILED);
    }

    @Test
    void testMarkDriverReady(){
        OnboardingEntity onboardingEntity = DummyData.getOnboardingEntity(1, OnboardingModule.ONBOARDED, ModuleStatus.SUCCESS);
        DriverEntity driverEntity = DummyData.getOnboardedDriver(0, onboardingEntity);

        Integer driverID = onboardingEntity.getId();

        when(pendingOnboardingRepository.getReferenceById(driverID)).thenReturn(onboardingEntity);
        when(driverRepository.save(any(DriverEntity.class))).thenReturn(driverEntity);
        driverOnboardingService.markDriverReady(driverID);

        verify(pendingOnboardingRepository, times(1)).getReferenceById(driverID);
        verify(driverRepository, times(1)).save(any(DriverEntity.class));
    }

    @Test
    void testMarkDriverReadyOfNonOnboardedDriver(){
        OnboardingEntity onboardingEntity = DummyData.getOnboardingEntity(1, OnboardingModule.DOCUMENT_COLLECTION, ModuleStatus.IN_PROGRESS);

        int driverID = onboardingEntity.getId();

        when(pendingOnboardingRepository.getReferenceById(driverID)).thenReturn(onboardingEntity);

        assertThrows(ValidationException.class, () -> driverOnboardingService.markDriverReady(driverID));

        verify(pendingOnboardingRepository, times(1)).getReferenceById(driverID);
    }
}
