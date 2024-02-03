package com.acme.onboarding.database.repository;

import com.acme.onboarding.database.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, String> {
    VehicleEntity findByManufacturerAndModel(String manufacturer, String model);
}
