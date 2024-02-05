package com.acme.onboarding.service.implementation;

import com.acme.onboarding.service.interfaces.IExternalService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ExternalService implements IExternalService {

    private static final int timeout = 2;

    @Override
    public boolean documentCollection(Integer driverId) throws InterruptedException {
        TimeUnit.SECONDS.sleep(timeout);
        return true;
    }

    @Override
    public boolean backgroundVerification(Integer driverId) throws InterruptedException {
        TimeUnit.SECONDS.sleep(timeout);
        return true;
    }

    @Override
    public boolean trackerShipping(Integer driverId) throws InterruptedException {
        TimeUnit.SECONDS.sleep(timeout);
        return true;
    }
}
