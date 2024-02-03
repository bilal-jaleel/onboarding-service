package com.acme.onboarding.service.model;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record Vehicle(Integer id,
                      @NotNull  String manufacturer,
                      @NotNull String model,
                      String type) {
}
