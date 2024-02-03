package com.acme.onboarding.service.interfaces;

import com.acme.onboarding.database.entity.PendingDriverOnboardingEntity;
import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import com.acme.onboarding.service.model.Driver;

import java.util.ArrayList;

public interface IDriverOnboardingService {

    Driver register(Driver driver);

    PendingDriverOnboardingEntity getDriverOnboardingStatus(Integer id);

    boolean markModuleStatusAndTriggerNextModule(int driverID, OnboardingModule module, ModuleStatus status) ;

    void markDriverReady(int driverID);

}
