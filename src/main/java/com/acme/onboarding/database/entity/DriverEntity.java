
package com.acme.onboarding.database.entity;


import com.acme.onboarding.service.model.Address;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "drivers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverEntity {

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
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    VehicleEntity vehicleEntity;

}

