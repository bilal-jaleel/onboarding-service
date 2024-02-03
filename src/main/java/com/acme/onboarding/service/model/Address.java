package com.acme.onboarding.service.model;


import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Embeddable
public record Address(@NotNull  String line1,
                      @NotNull String line2,
                      @NotNull String landmark,
                      @NotNull String pincode,
                      @NotNull String state,
                      @NotNull String country) {
}
