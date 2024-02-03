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
@Table(name = "onboarding")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingEntity {
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
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    @ToString.Exclude
    VehicleEntity vehicleEntity;

    @OneToOne(mappedBy = "onboardingEntity")
    @JsonManagedReference
    @ToString.Exclude
    DriverEntity driverEntity;
}
