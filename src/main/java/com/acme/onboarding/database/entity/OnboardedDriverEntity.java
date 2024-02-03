
package com.acme.onboarding.database.entity;


import com.acme.onboarding.service.model.Address;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "onboarded_drivers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardedDriverEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;

    String name;

    String email;

    String mobile;

    @JdbcTypeCode(SqlTypes.JSON)
    Address address;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    RideEntity ride;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "onboarding_id", referencedColumnName = "id")
    PendingDriverOnboardingEntity pendingDriverOnboardingEntity;
}

