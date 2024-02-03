package com.acme.onboarding.database.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "rides")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RideEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;

    String manufacturer;
    String model;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "ride")
    List<PendingDriverOnboardingEntity> pendingDriverOnboardingEntities;

    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "ride")
    List<OnboardedDriverEntity> onboardedDriverEntities;
}
