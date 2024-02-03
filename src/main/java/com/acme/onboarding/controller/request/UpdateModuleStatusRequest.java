package com.acme.onboarding.controller.request;

import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;


public record UpdateModuleStatusRequest(Integer id, OnboardingModule onboardingModule, ModuleStatus moduleStatus) {}
