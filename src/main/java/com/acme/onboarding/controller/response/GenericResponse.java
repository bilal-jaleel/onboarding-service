package com.acme.onboarding.controller.response;

import lombok.Builder;

@Builder
public record GenericResponse<T>(boolean status, T data) {
}
