package com.acme.onboarding.service.interfaces;

import com.acme.onboarding.database.entity.OnboardingEntity;
import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import com.acme.onboarding.service.model.Driver;

public interface IDriverOnboardingService {

    Driver register(Driver driver);

    OnboardingEntity getDriverOnboardingStatus(Integer id);

    void updateModuleStatus(int driverID, OnboardingModule module, ModuleStatus status) ;

    void markDriverReady(int driverID);

}
