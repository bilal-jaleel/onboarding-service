package com.acme.onboarding.controller.request;

import com.acme.onboarding.service.model.Address;
import com.acme.onboarding.service.model.Ride;

public record RegisterDriverRequest(String name, String email, String mobile, Address address, Ride ride) {
}
