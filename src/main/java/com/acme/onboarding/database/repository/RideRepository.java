package com.acme.onboarding.database.repository;

import com.acme.onboarding.database.entity.RideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RideRepository extends JpaRepository<RideEntity, String> {
    RideEntity findByManufacturerAndModel(String manufacturer, String model);
}
