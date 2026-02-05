package com.bemicroservices.user_service.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "addresses")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Adress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String phoneNumber;

    String street;
    String city;
    String state;
    String country;
    String zipCode;
    String description;

    @Column(nullable = false)
    Boolean isDefault = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    User user;
}
