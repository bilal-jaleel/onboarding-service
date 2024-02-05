package com.acme.onboarding.controller.request;

import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record UpdateModuleStatusRequest(@NotNull Integer id, @NotNull OnboardingModule onboardingModule, @NotNull ModuleStatus moduleStatus) {}
