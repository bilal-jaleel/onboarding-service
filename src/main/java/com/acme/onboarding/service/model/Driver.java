package com.acme.onboarding.service.model;


import lombok.Builder;
import lombok.Data;

@Builder
public record Driver(String name, String email, String mobile, Address address, Ride ride) {
}
