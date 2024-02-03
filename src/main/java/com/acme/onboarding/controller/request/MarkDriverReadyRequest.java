package com.acme.onboarding.controller.request;

import jakarta.validation.constraints.NotNull;

public record MarkDriverReadyRequest(@NotNull Integer id) {
}
