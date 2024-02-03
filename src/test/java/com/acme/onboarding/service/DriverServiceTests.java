package com.acme.onboarding.service;

import com.acme.onboarding.database.entity.OnboardedDriverEntity;
import com.acme.onboarding.database.entity.PendingDriverOnboardingEntity;
import com.acme.onboarding.database.entity.RideEntity;
import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import com.acme.onboarding.database.repository.OnboardedDriverRepository;
import com.acme.onboarding.database.repository.PendingDriverOnboardingRepository;
import com.acme.onboarding.database.repository.RideRepository;
import com.acme.onboarding.exceptions.ValidationException;
import com.acme.onboarding.service.implementation.DriverOnboardingService;
import com.acme.onboarding.service.model.Driver;
import com.acme.onboarding.utils.DummyData;
import com.acme.onboarding.utils.Mapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class DriverServiceTests {


    @Mock
    private PendingDriverOnboardingRepository pendingOnboardingRepository;

    @Mock
    private RideRepository rideRepository;

    @Mock
    private OnboardedDriverRepository onboardedDriverRepository;

    @Autowired
    @InjectMocks
    private DriverOnboardingService driverOnboardingService;


    @Test
    void createDriver() {
        Driver driver = DummyData.getDriver();
        PendingDriverOnboardingEntity pendingDriverOnboardingEntity = DummyData.getDriverEntity(null, null, null);
        RideEntity rideEntity = DummyData.getRideEntity();

        when(pendingOnboardingRepository.save(pendingDriverOnboardingEntity)).thenReturn(pendingDriverOnboardingEntity);
        when(rideRepository.findByManufacturerAndModel(rideEntity.getManufacturer(), rideEntity.getModel())).thenReturn(rideEntity);

        Driver savedDriver = driverOnboardingService.register(driver);
        assertEquals(driver.name(), savedDriver.name());
        assertEquals(driver.email(), savedDriver.email());
        assertEquals(driver.mobile(), savedDriver.mobile());
        verify(pendingOnboardingRepository, times(1)).save(pendingDriverOnboardingEntity);
    }

    @Test
    void getAllDrivers() {
        ArrayList<Driver> driverList = new ArrayList<>(Collections.singletonList(DummyData.getDriver()));
        ArrayList<PendingDriverOnboardingEntity> dummyDatas = new ArrayList<>(Collections.singletonList(Mapper.mapDriverToEntity(driverList.getFirst())));

        when(pendingOnboardingRepository.findAll()).thenReturn(dummyDatas);

        ArrayList<PendingDriverOnboardingEntity> drivers = driverOnboardingService.getAllDrivers();

        assertEquals(drivers.getFirst().getName(), driverList.getFirst().name());
        assertEquals(drivers.getFirst().getEmail(), driverList.getFirst().email());
        assertEquals(drivers.getFirst().getMobile(), driverList.getFirst().mobile());
        verify(pendingOnboardingRepository, times(1)).findAll();
    }


    @Test
    void markDocumentCollectionCompleted() {

        PendingDriverOnboardingEntity pendingDriverOnboardingEntity = DummyData.getDriverEntity(1, OnboardingModule.DOCUMENT_COLLECTION, ModuleStatus.SUCCESS);

        int driverID = pendingDriverOnboardingEntity.getId();

        when(pendingOnboardingRepository.updateModuleStatusForDriver(driverID, OnboardingModule.BACKGROUND_VERIFICATION, ModuleStatus.IN_PROGRESS)).thenReturn(1);
        when(pendingOnboardingRepository.getReferenceById(driverID)).thenReturn(pendingDriverOnboardingEntity);

        driverOnboardingService.markModuleStatusAndTriggerNextModule(driverID, OnboardingModule.DOCUMENT_COLLECTION, ModuleStatus.SUCCESS);

        verify(pendingOnboardingRepository, times(1)).updateModuleStatusForDriver(driverID, OnboardingModule.BACKGROUND_VERIFICATION, ModuleStatus.IN_PROGRESS);
    }

    @Test
    void markUnrelatedModuleStatus() {

        PendingDriverOnboardingEntity pendingDriverOnboardingEntity = DummyData.getDriverEntity(1, OnboardingModule.DOCUMENT_COLLECTION, ModuleStatus.IN_PROGRESS);

        int driverID = pendingDriverOnboardingEntity.getId();

        when(pendingOnboardingRepository.getReferenceById(driverID)).thenReturn(pendingDriverOnboardingEntity);

        assertThrows(ValidationException.class,
                () -> driverOnboardingService.markModuleStatusAndTriggerNextModule(driverID, OnboardingModule.BACKGROUND_VERIFICATION, ModuleStatus.SUCCESS)
        );
        verify(pendingOnboardingRepository, times(1)).getReferenceById(driverID);
    }

    @Test
    void markOnboardedDriverStatus() {

        PendingDriverOnboardingEntity pendingDriverOnboardingEntity = DummyData.getDriverEntity(1, OnboardingModule.ONBOARDED, ModuleStatus.SUCCESS);

        int driverID = pendingDriverOnboardingEntity.getId();

        when(pendingOnboardingRepository.getReferenceById(driverID)).thenReturn(pendingDriverOnboardingEntity);

        assertThrows(ValidationException.class,
                () -> driverOnboardingService.markModuleStatusAndTriggerNextModule(driverID, OnboardingModule.BACKGROUND_VERIFICATION, ModuleStatus.SUCCESS)
        );
        verify(pendingOnboardingRepository, times(1)).getReferenceById(driverID);
    }

    @Test
    void markDriverAsOnboarded() {

        PendingDriverOnboardingEntity pendingDriverOnboardingEntity = DummyData.getDriverEntity(1, OnboardingModule.TRACKER_SHIPPING, ModuleStatus.IN_PROGRESS);

        int driverID = pendingDriverOnboardingEntity.getId();

        when(pendingOnboardingRepository.getReferenceById(driverID)).thenReturn(pendingDriverOnboardingEntity);
        when(pendingOnboardingRepository.updateModuleStatusForDriver(driverID, OnboardingModule.ONBOARDED, ModuleStatus.SUCCESS)).thenReturn(1);

        boolean success = driverOnboardingService.markModuleStatusAndTriggerNextModule(driverID, OnboardingModule.TRACKER_SHIPPING, ModuleStatus.SUCCESS);
        assertTrue(success);
        verify(pendingOnboardingRepository, times(1)).getReferenceById(driverID);
        verify(pendingOnboardingRepository, times(1)).updateModuleStatusForDriver(driverID,OnboardingModule.ONBOARDED, ModuleStatus.SUCCESS);
    }

    @Test
    void markModuleFailed() {
        PendingDriverOnboardingEntity pendingDriverOnboardingEntity = DummyData.getDriverEntity(1, OnboardingModule.TRACKER_SHIPPING, ModuleStatus.IN_PROGRESS);
        int driverID = pendingDriverOnboardingEntity.getId();

        when(pendingOnboardingRepository.getReferenceById(driverID)).thenReturn(pendingDriverOnboardingEntity);
        when(pendingOnboardingRepository.updateModuleStatusForDriver(driverID, OnboardingModule.TRACKER_SHIPPING, ModuleStatus.FAILED)).thenReturn(1);

        boolean success = driverOnboardingService.markModuleStatusAndTriggerNextModule(driverID, OnboardingModule.TRACKER_SHIPPING, ModuleStatus.FAILED);

        assertTrue(success);
        verify(pendingOnboardingRepository, times(1)).getReferenceById(driverID);
        verify(pendingOnboardingRepository, times(1)).updateModuleStatusForDriver(driverID,OnboardingModule.TRACKER_SHIPPING, ModuleStatus.FAILED);
    }

    @Test
    void markDriverReady(){
        PendingDriverOnboardingEntity pendingDriverOnboardingEntity = DummyData.getDriverEntity(1, OnboardingModule.ONBOARDED, ModuleStatus.SUCCESS);
        OnboardedDriverEntity onboardedDriverEntity = DummyData.getOnboardedDriver(0, pendingDriverOnboardingEntity);

        int driverID = pendingDriverOnboardingEntity.getId();

        when(pendingOnboardingRepository.getReferenceById(driverID)).thenReturn(pendingDriverOnboardingEntity);
        when(onboardedDriverRepository.save(onboardedDriverEntity)).thenReturn(onboardedDriverEntity);
        driverOnboardingService.markDriverReady(driverID);

        verify(pendingOnboardingRepository, times(1)).getReferenceById(driverID);
        verify(onboardedDriverRepository, times(1)).save(onboardedDriverEntity);
    }

    @Test
    void markNonOnboardedDriverReady(){
        PendingDriverOnboardingEntity pendingDriverOnboardingEntity = DummyData.getDriverEntity(1, OnboardingModule.DOCUMENT_COLLECTION, ModuleStatus.IN_PROGRESS);
        OnboardedDriverEntity onboardedDriverEntity = DummyData.getOnboardedDriver(0, pendingDriverOnboardingEntity);

        int driverID = pendingDriverOnboardingEntity.getId();

        when(pendingOnboardingRepository.getReferenceById(driverID)).thenReturn(pendingDriverOnboardingEntity);

        assertThrows(ValidationException.class, () -> driverOnboardingService.markDriverReady(driverID));

        verify(pendingOnboardingRepository, times(1)).getReferenceById(driverID);
    }
}
