package com.acme.onboarding.service.interfaces;

public interface IExternalService {

    boolean documentCollection(Integer driverId) throws InterruptedException;
    boolean backgroundVerification(Integer driverId) throws InterruptedException;
    boolean trackerShipping(Integer driverId) throws InterruptedException;
}
