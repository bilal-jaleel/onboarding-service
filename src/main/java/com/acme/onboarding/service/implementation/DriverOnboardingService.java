package com.acme.onboarding.service.implementation;

import com.acme.onboarding.database.entity.DriverEntity;
import com.acme.onboarding.database.entity.OnboardingEntity;
import com.acme.onboarding.database.entity.VehicleEntity;
import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import com.acme.onboarding.database.repository.DriverRepository;
import com.acme.onboarding.database.repository.OnboardingRepository;
import com.acme.onboarding.database.repository.VehicleRepository;
import com.acme.onboarding.service.exceptions.ExternalServiceFailureException;
import com.acme.onboarding.service.interfaces.IDriverOnboardingService;
import com.acme.onboarding.service.interfaces.IExternalService;
import com.acme.onboarding.service.model.Driver;
import com.acme.onboarding.utils.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.validation.ValidationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class DriverOnboardingService implements IDriverOnboardingService {

    // TODO: Repositories can be abstracted into an interface for db calls
    @Autowired
    OnboardingRepository onboardingRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    IExternalService externalService;

    @Override
    public Driver register(Driver driver) throws InterruptedException {
        // Get the vehicle of the driver
        VehicleEntity vehicleEntity = vehicleRepository.findByManufacturerAndModel(driver.vehicle().manufacturer(), driver.vehicle().model());

        // Vehicle does not exist in the db
        if(vehicleEntity == null){
            throw new ValidationException("Invalid vehicle selected");
        }

        // Create onboarding entity from driver object
        OnboardingEntity onboardingEntity = Mapper.mapDriverToOnboardingEntity(driver,
                OnboardingModule.DOCUMENT_COLLECTION,
                ModuleStatus.IN_PROGRESS,
                vehicleEntity);

        OnboardingEntity registerdOnboardingEntity = onboardingRepository.save(onboardingEntity);
        // Todo: Trigger Driver Onboarding Procedures
        externalService.documentCollection(registerdOnboardingEntity.getId());
        return Mapper.mapOnboardingEntityToDriver(registerdOnboardingEntity);
    }

    @Override
    public OnboardingEntity getDriverOnboardingStatus(Integer id) {
        return onboardingRepository.getReferenceById(id);
    }

    @Override
    public void updateModuleStatus(int driverID, OnboardingModule module, ModuleStatus status) throws InterruptedException, ExternalServiceFailureException {
        OnboardingEntity currentDriverOnboardingEntity = onboardingRepository.getReferenceById(driverID);

        //If the status being updated is to in_progress, that is invalid
        if (status == ModuleStatus.IN_PROGRESS) {
            log.error("The module is already in progress");
            throw new ValidationException("The module is already in progress");
        }

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
            onboardingRepository.updateModuleStatusForDriver(driverID, module, status, currentDriverOnboardingEntity.getCompletedModules());
            return;
        }
        ArrayList<String> completedModules = currentDriverOnboardingEntity.getCompletedModules() != null ? currentDriverOnboardingEntity.getCompletedModules() : new ArrayList<>();
        completedModules.add(currentDriverOnboardingEntity.getModule().name());

        OnboardingModule nextModule =
                OnboardingModule.values()[currentDriverOnboardingEntity.getModule().ordinal() + 1];

        // If next module is onboarded, there are no more services to trigger
        if (nextModule == OnboardingModule.ONBOARDED) {
            onboardingRepository.updateModuleStatusForDriver(driverID, OnboardingModule.ONBOARDED, ModuleStatus.SUCCESS, completedModules);
            return;
        }

        // mark the next module as started
        onboardingRepository.updateModuleStatusForDriver(driverID, nextModule, ModuleStatus.START, completedModules);

        int retry = 0;
        while (retry < 3 && !callServiceForModule(nextModule, driverID)){
            retry++;
        }

        if (retry >= 3){
            String message = "External service for module: "+ module + " unreachable for driver with id: " + driverID ;
            log.error(message);
           throw new ExternalServiceFailureException(message);
        }

        onboardingRepository.updateModuleStatusForDriver(driverID, nextModule, ModuleStatus.IN_PROGRESS, completedModules);
    }

    @Override
    public void markDriverReady(int driverID) {
        // Get onboarding status for the driver
        OnboardingEntity onboardingEntity = onboardingRepository.getReferenceById(driverID);

        // Check if driver is not onboarded
        if (onboardingEntity.getModule() != OnboardingModule.ONBOARDED) {
            throw new ValidationException("The driver must be onboarded before he can be ready to start a ride");
        }
        DriverEntity driverEntity = DriverEntity.builder()
                .email(onboardingEntity.getEmail())
                .address(onboardingEntity.getAddress())
                .mobile(onboardingEntity.getMobile())
                .name(onboardingEntity.getName())
                .vehicleEntity(onboardingEntity.getVehicleEntity())
                .build();

        // Move the onboarded driver to driver table
        driverRepository.save(driverEntity);
    }

    private boolean callServiceForModule(OnboardingModule module, Integer driverId) throws InterruptedException {
        return switch (module){
            // Call Document Collection Service
            case DOCUMENT_COLLECTION -> externalService.documentCollection(driverId);
            // Call Background Verification Service
            case BACKGROUND_VERIFICATION -> externalService.backgroundVerification(driverId);
            // Call Tracker Shipping Service
            case TRACKER_SHIPPING -> externalService.trackerShipping(driverId);
            // No more service to call, hence return true
            case ONBOARDED -> true;
        };
    }


}
