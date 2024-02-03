package com.acme.onboarding.database.repository;

import com.acme.onboarding.database.entity.OnboardedDriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnboardedDriverRepository extends JpaRepository<OnboardedDriverEntity, Integer> { }
