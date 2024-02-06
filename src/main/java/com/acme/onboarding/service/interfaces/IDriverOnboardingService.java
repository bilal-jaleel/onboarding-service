package com.acme.onboarding.service.interfaces;

import com.acme.onboarding.database.entity.OnboardingEntity;
import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import com.acme.onboarding.service.exceptions.ExternalServiceFailureException;
import com.acme.onboarding.service.model.Driver;

public interface IDriverOnboardingService {

    Driver register(Driver driver) throws InterruptedException;

    OnboardingEntity getDriverOnboardingStatus(Integer id);

    void updateModuleStatus(int driverID, OnboardingModule module, ModuleStatus status) throws InterruptedException, ExternalServiceFailureException;

    void markDriverReady(int driverID);

}
