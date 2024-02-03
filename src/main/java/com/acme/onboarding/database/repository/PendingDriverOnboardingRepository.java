package com.acme.onboarding.database.repository;

import com.acme.onboarding.database.entity.PendingDriverOnboardingEntity;
import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingDriverOnboardingRepository extends JpaRepository<PendingDriverOnboardingEntity, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE PendingDriverOnboardingEntity p set p.module = :module, p.moduleStatus = :status where p.id = :driverID")
    Integer updateModuleStatusForDriver(int driverID, OnboardingModule module, ModuleStatus status);
}
