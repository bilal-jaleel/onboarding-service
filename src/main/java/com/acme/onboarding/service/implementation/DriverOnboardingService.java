package com.acme.onboarding.service.implementation;

import com.acme.onboarding.database.entity.DriverEntity;
import com.acme.onboarding.database.entity.OnboardingEntity;
import com.acme.onboarding.database.entity.VehicleEntity;
import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import com.acme.onboarding.database.repository.DriverRepository;
import com.acme.onboarding.database.repository.OnboardingRepository;
import com.acme.onboarding.database.repository.VehicleRepository;
import com.acme.onboarding.service.interfaces.IDriverOnboardingService;
import com.acme.onboarding.service.model.Driver;
import com.acme.onboarding.utils.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.validation.ValidationException;

@Service
@Slf4j
public class DriverOnboardingService implements IDriverOnboardingService {

    @Autowired
    OnboardingRepository onboardingRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    DriverRepository driverRepository;

    @Override
    public Driver register(Driver driver) {
        VehicleEntity vehicleEntity = vehicleRepository.findByManufacturerAndModel(driver.vehicle().manufacturer(), driver.vehicle().model());
        OnboardingEntity onboardingEntity = Mapper.mapDriverToOnboardingEntity(driver);

        onboardingEntity.setVehicleEntity(vehicleEntity);
        onboardingEntity.setModule(OnboardingModule.DOCUMENT_COLLECTION);
        onboardingEntity.setModuleStatus(ModuleStatus.IN_PROGRESS);

        OnboardingEntity registerdOnboardingEntity = onboardingRepository.save(onboardingEntity);
        // Todo: Trigger Driver Onboarding Procedures
        return Mapper.mapOnboardingEntityToDriver(registerdOnboardingEntity);
    }

    @Override
    public OnboardingEntity getDriverOnboardingStatus(Integer id) {
        return onboardingRepository.getReferenceById(id);
    }

    @Override
    public void updateModuleStatus(int driverID, OnboardingModule module, ModuleStatus status) {
        OnboardingEntity currentDriverOnboardingEntity = onboardingRepository.getReferenceById(driverID);

        //If the status being updated is to in_progress, that is invalid
        if(status == ModuleStatus.IN_PROGRESS){
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
            onboardingRepository.updateModuleStatusForDriver(driverID, module, status);
            return;
        }

        OnboardingModule nextPossibleModule =
                OnboardingModule.values()[currentDriverOnboardingEntity.getModule().ordinal() + 1];
        ModuleStatus nextModuleStatus = ModuleStatus.IN_PROGRESS;
        if (nextPossibleModule == OnboardingModule.ONBOARDED) {
            nextModuleStatus = ModuleStatus.SUCCESS;
        }

        // mark & trigger next module
        onboardingRepository.updateModuleStatusForDriver(driverID, nextPossibleModule, nextModuleStatus);
    }

    @Override
    public void markDriverReady(int driverID) {
        OnboardingEntity onboardingEntity = onboardingRepository.getReferenceById(driverID);
        if(onboardingEntity.getModule() != OnboardingModule.ONBOARDED){
           throw new ValidationException("The driver must be onboarded before he can be ready to start a ride");
        }
        DriverEntity driverEntity = DriverEntity.builder()
                .email(onboardingEntity.getEmail())
                .address(onboardingEntity.getAddress())
                .mobile(onboardingEntity.getMobile())
                .name(onboardingEntity.getName())
                .vehicleEntity(onboardingEntity.getVehicleEntity())
                .onboardingEntity(onboardingEntity)
                .build();

        driverRepository.save(driverEntity);
    }


}
