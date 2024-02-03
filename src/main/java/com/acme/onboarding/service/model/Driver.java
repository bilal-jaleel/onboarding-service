package com.acme.onboarding.service.model;


import lombok.Builder;
import lombok.Setter;

@Builder
public record Driver(Integer id,String name, String email, String mobile, Address address, Vehicle vehicle) {
}
