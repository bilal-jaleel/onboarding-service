package com.acme.onboarding.service.model;


import lombok.Builder;

@Builder
public record Ride(String manufacturer, String model) {
}
