package com.acme.onboarding.service.model;


import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Data;

@Builder
@Embeddable
public record Address(String line1, String line2, String landmark, String pincode, String state, String country) {
}
