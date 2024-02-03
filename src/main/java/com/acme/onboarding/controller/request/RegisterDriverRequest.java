package com.acme.onboarding.controller.request;

import com.acme.onboarding.service.model.Address;
import com.acme.onboarding.service.model.Vehicle;
import jakarta.validation.Valid;
import lombok.Builder;
import jakarta.validation.constraints.NotNull;

@Builder
public record RegisterDriverRequest(@NotNull String name,
                                    @NotNull  String email,
                                    @NotNull String mobile,
                                    @Valid Address address, @Valid Vehicle vehicle) {
}
