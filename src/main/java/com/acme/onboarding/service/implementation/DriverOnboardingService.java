package com.acme.onboarding.service.implementation;

import com.acme.onboarding.database.entity.OnboardedDriverEntity;
import com.acme.onboarding.database.entity.PendingDriverOnboardingEntity;
import com.acme.onboarding.database.entity.RideEntity;
import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import com.acme.onboarding.database.repository.OnboardedDriverRepository;
import com.acme.onboarding.database.repository.PendingDriverOnboardingRepository;
import com.acme.onboarding.database.repository.RideRepository;
import com.acme.onboarding.exceptions.ValidationException;
import com.acme.onboarding.service.interfaces.IDriverOnboardingService;
import com.acme.onboarding.service.model.Driver;
import com.acme.onboarding.utils.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class DriverOnboardingService implements IDriverOnboardingService {

    @Autowired
    PendingDriverOnboardingRepository pendingOnboardingRepository;

    @Autowired
    RideRepository rideRepository;

    @Autowired
    OnboardedDriverRepository onboardedDriverRepository;

    @Override
    public Driver register(Driver driver) {
        RideEntity rideEntity = rideRepository.findByManufacturerAndModel(driver.ride().manufacturer(), driver.ride().model());
        PendingDriverOnboardingEntity pendingDriverOnboardingEntity = Mapper.mapDriverToEntity(driver);

        pendingDriverOnboardingEntity.setRide(rideEntity);
        pendingDriverOnboardingEntity.setModule(OnboardingModule.DOCUMENT_COLLECTION);
        pendingDriverOnboardingEntity.setModuleStatus(ModuleStatus.IN_PROGRESS);

        pendingOnboardingRepository.save(pendingDriverOnboardingEntity);
        // Todo: Trigger Driver Onboarding Procedures
        return driver;
    }

    @Override
    public ArrayList<PendingDriverOnboardingEntity> getAllDrivers() {
        return new ArrayList<>(pendingOnboardingRepository.findAll());
    }

    @Override
    public boolean markModuleStatusAndTriggerNextModule(int driverID, OnboardingModule module, ModuleStatus status) {
        PendingDriverOnboardingEntity currentDriverOnboardingEntity = pendingOnboardingRepository.getReferenceById(driverID);

        //If the driver is already onboarded, then break
        if (currentDriverOnboardingEntity.getModule() == OnboardingModule.ONBOARDED) {
            log.error("Onboarding for driver with id " + driverID + " is already completed");
            throw new ValidationException("Onboarding for this driver is already completed");
        }

        // If the updation is not happening for the module the user is in right now,
        // then break since it is an invalid update
        if (currentDriverOnboardingEntity.getModule() != module
                && currentDriverOnboardingEntity.getModuleStatus() == ModuleStatus.IN_PROGRESS) {
            log.error("Invalid transition to module");
            throw new ValidationException("Invalid transition to module");
        }


        // If the current module has failed, then update the status and return
        if (status == ModuleStatus.FAILED) {
            int updatedRows = pendingOnboardingRepository.updateModuleStatusForDriver(driverID, module, status);
            return updatedRows == 1;
        }

        OnboardingModule nextPossibleModule =
                OnboardingModule.values()[currentDriverOnboardingEntity.getModule().ordinal() + 1];
        ModuleStatus nextModuleStatus = ModuleStatus.IN_PROGRESS;
        if (nextPossibleModule == OnboardingModule.ONBOARDED) {
            nextModuleStatus = ModuleStatus.SUCCESS;
        }

        // mark & trigger next module
        int updatedRows = pendingOnboardingRepository.updateModuleStatusForDriver(driverID, nextPossibleModule, nextModuleStatus);
        return updatedRows == 1;
    }

    @Override
    public void markDriverReady(int driverID) {
        PendingDriverOnboardingEntity pendingDriverOnboardingEntity = pendingOnboardingRepository.getReferenceById(driverID);
        if(pendingDriverOnboardingEntity.getModule() != OnboardingModule.ONBOARDED){
           throw new ValidationException("The driver must be onboarded before he can be ready to start a ride");
        }
        OnboardedDriverEntity onboardedDriverEntity = OnboardedDriverEntity.builder()
                .email(pendingDriverOnboardingEntity.getEmail())
                .address(pendingDriverOnboardingEntity.getAddress())
                .mobile(pendingDriverOnboardingEntity.getMobile())
                .name(pendingDriverOnboardingEntity.getName())
                .ride(pendingDriverOnboardingEntity.getRide())
                .pendingDriverOnboardingEntity(pendingDriverOnboardingEntity)
                .build();

        onboardedDriverRepository.save(onboardedDriverEntity);
    }


}
