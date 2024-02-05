package com.acme.onboarding.database.entity;

import com.acme.onboarding.database.enums.ModuleStatus;
import com.acme.onboarding.database.enums.OnboardingModule;
import com.acme.onboarding.service.model.Address;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    OnboardingModule module;

    @Enumerated(EnumType.STRING)
    ModuleStatus moduleStatus;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    @ToString.Exclude
    VehicleEntity vehicleEntity;

    @JdbcTypeCode(SqlTypes.JSON)
    ArrayList<String> completedModules;

}
