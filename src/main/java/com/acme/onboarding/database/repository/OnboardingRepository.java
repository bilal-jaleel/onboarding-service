package com.acme.onboarding.database.repository;

import com.acme.onboarding.database.entity.OnboardingEntity;
import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnboardingRepository extends JpaRepository<OnboardingEntity, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE OnboardingEntity o set o.module = :module, o.moduleStatus = :status, o.completedModules = :completedModules where o.id = :driverID")
    void updateModuleStatusForDriver(Integer driverID, OnboardingModule module, ModuleStatus status, List<String> completedModules);
}
