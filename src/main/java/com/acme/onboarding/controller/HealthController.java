package com.acme.onboarding.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HealthController {
    @GetMapping("/health")
    String health(){
        return "OK";
    }

}
