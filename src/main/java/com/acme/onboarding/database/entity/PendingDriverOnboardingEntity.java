package com.acme.onboarding.database.entity;

import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import com.acme.onboarding.service.model.Address;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "pending_onboarding_drivers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingDriverOnboardingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Integer id;

    String name;

    String email;

    String mobile;

    @JdbcTypeCode(SqlTypes.JSON)
    Address address;

    OnboardingModule module;

    ModuleStatus moduleStatus;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    @ToString.Exclude
    RideEntity ride;

    @OneToOne(mappedBy = "pendingDriverOnboardingEntity")
    @JsonManagedReference
    @ToString.Exclude
    OnboardedDriverEntity onboardedDriverEntity;
}
