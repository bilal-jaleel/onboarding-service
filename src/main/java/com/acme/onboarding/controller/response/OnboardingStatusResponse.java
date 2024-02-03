package com.acme.onboarding.controller.response;

import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import lombok.Builder;

@Builder
public record OnboardingStatusResponse(Integer id, OnboardingModule module, ModuleStatus status) {
}
